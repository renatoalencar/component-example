(defproject example "0.0.0"
  :main nil
  :aliases {"run-atom" ["run" "-m" "example-atom"]
            "run-component" ["run" "-m" "example-component"]}
  :source-paths ["."]
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [com.novemberain/monger "3.1.0"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [com.stuartsierra/component "1.0.0"]])
