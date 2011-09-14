(defproject foodhunter "1.0.0-SNAPSHOT"
  :description "FIXME: write"
  :dependencies [[org.clojure/clojure "1.2.1"]                 
                 [noir "1.1.1-SNAPSHOT"]]
  :dev-dependencies [[lein-ring "0.4.5"]]
  :war-resources-path "resources/public"  
  :ring {:handler servlets.main/app})