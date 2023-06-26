(defproject versionranges "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [spork "0.2.1.7-SNAPSHOT"
                  :exclusions [org.clojure/tools.reader]]
                 [software.amazon.awscdk/cdk-asset-node-proxy-agent-v5
                  "2.0.148"
                  :exclusions
                  [software.amazon.jsii/jsii-runtime
                   org.jetbrains/annotations
                   javax.annotation/javax.annotation-api]]
                 [software.amazon.jsii/jsii-runtime
                  "1.84.0"
                  :exclusions
                  [javax.annotation/javax.annotation-api
                   com.fasterxml.jackson.core/jackson-core
                   org.jetbrains/annotations
                   com.fasterxml.jackson.core/jackson-databind
                   org.zeroturnaround/zt-exec
                   com.fasterxml.jackson.datatype/jackson-datatype-jsr310]]
                 [com.fasterxml.jackson.core/jackson-databind "2.11.3"]
                 [com.fasterxml.jackson.core/jackson-core "2.11.3"]
                 [software.amazon.awscdk/cdk-asset-kubectl-v20
                  "2.1.1"
                  :exclusions
                  [software.amazon.jsii/jsii-runtime
                   org.jetbrains/annotations
                   javax.annotation/javax.annotation-api]]
                 [org.jetbrains/annotations "16.0.3"]
                 [software.amazon.awscdk/cdk-asset-awscli-v1
                  "2.2.177"
                  :exclusions
                  [software.amazon.jsii/jsii-runtime
                   org.jetbrains/annotations
                   javax.annotation/javax.annotation-api]]
                 [software.constructs/constructs
                  "10.0.0"
                  :exclusions
                  [software.amazon.jsii/jsii-runtime
                   javax.annotation/javax.annotation-api
                   org.jetbrains/annotations]]
                 [javax.annotation/javax.annotation-api "1.3.2"]
                 [com.fasterxml.jackson.datatype/jackson-datatype-jsr310 "2.11.3"]
                 [org.zeroturnaround/zt-exec "1.12"]
                 [software.amazon.awscdk/aws-cdk-lib
                  "2.85.0"
                  :exclusions
                  [software.amazon.awscdk/cdk-asset-awscli-v1
                   software.amazon.awscdk/cdk-asset-kubectl-v20
                   software.amazon.awscdk/cdk-asset-node-proxy-agent-v5
                   software.constructs/constructs
                   software.amazon.jsii/jsii-runtime
                   org.jetbrains/annotations
                   javax.annotation/javax.annotation-api]]])
