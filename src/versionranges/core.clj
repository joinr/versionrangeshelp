(ns versionranges.core
  (:require [spork.cljgraph.core :as g]))

(def xs (-> "deps.txt" slurp clojure.string/split-lines))

(def artifacts (into [] (partition 2 xs)))

(defn parse-artifact [path]
  (->> (clojure.string/split path #"->")
       (mapv clojure.edn/read-string)))

(def consider (count "Consider using "))

(defn parse-consideration [path]
  (let [rec (->> (subs path consider (count path))
                 clojure.edn/read-string)]
    {:parent (vec   (take 2 rec))
     :child  (first (nth rec 3))
     :dep rec}))

(defn parse [[x y]]
  (let [p  (parse-artifact x)
        ch (parse-consideration y)]
    (assoc ch :version (-> p last second)
              :path p)))

;;want to build an output of
;;transitive dependencies and exclusions
;;so
;;parent software.amazon.awscdk/aws-cdk-lib "2.85.0"
;;immediate deps, if version ranged, then lock them in.
;;basically anything with a version range, just pick the lowest known range and
;;call it a day.
;;these are all leaves then (children).
;;need to build up deps from parent/child.
;;need parent/child relations.
;;walk the graph of parent/child, accumulating novel exclusions.

(defn accumulate [xs]
  (reduce (fn [gr {:keys [parent child dep version path]}]
            (->> path
                 (partition 2 1)
                 (reduce (fn [acc [p c]]
                           (g/conj-arc acc p c)) gr)))
          g/empty-graph xs))


(defn similars [gr]
  (->> gr
       g/nodes
       keys
       (group-by first)
       (reduce-kv (fn [acc k xs] (assoc acc k (into [] (map second xs)))) {})))

;;our strategy here will be to take the max of the min version.
;;ugh, this is brittle since it assumes all versions have an inclusive min version...

(defn as-range [x]
  (let [l-r (clojure.string/split x #",")]
    (if (= (count l-r) 2)
      (let [[l r] l-r]
        [(subs l 1 (count l))
         (subs r 0 (dec (count r)))])
      [(first l-r) (first l-r)])))

(defn simplify [xs]
  (->> (map as-range xs)
       (sort-by first)
       reverse
       first))

(defn resolved [g]
  (->> g
       similars
       (reduce-kv (fn [acc k xs] (assoc acc k (first (simplify xs)))) {})))

;;now we have concrete deps...
;;provide exclusions.

;;transform the graph.  compute a new graph where dependencies on the
;;prior artifact name are now concatenated into one.
;;so we need to group the nodes by their artifact, gather all the arcs,
;;then replace them with the concrete artifact.

;;we can walk from the root and emit arcs as we go.
;;building a new graph.
;;do a depth walk and emit new arcs based on the resolved arc names.
(defn exclusions [g]
  (let [leaves (resolved g)
        get-leaf (fn [k]
                   (when-let [v (leaves k)]
                     [k v]))]
    (loop [acc g/empty-graph
           remaining (->> (g/topsort g) (mapcat concat))]
      (if-let [[k v :as nd] (first remaining)]
        (let [new-node (or (get-leaf k) nd)
              sinks    (g/sinks g nd)
              new-arcs (for [[kold v :as dest] sinks]
                        (or (get-leaf kold)
                            dest))]
          (recur (reduce (fn [acc dest]
                           (g/conj-arc acc new-node dest)) acc new-arcs)
                 (rest remaining)))
        acc))))

;;for each parent, we want to pin exclusions to the child.
;;so we accumulate a deps of [parent :exclusions [children]]
(defn emit [exs]
  (reduce-kv (fn [acc parent children]
               (conj acc (if (seq children)
                           (conj parent :exclusions (mapv first (keys children)))
                           parent)))
             [] (-> exs :sinks)))
