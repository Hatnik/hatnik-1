(defproject hatnik.web "0.1.0-SNAPSHOT"
  :description "Web app for tracking library releases."
  :url "http://hatnik.com"
  :dependencies [; Clojure
                 [org.clojure/clojure "1.7.0-alpha5"]
                 [ring "1.3.2"]
                 [compojure "1.3.1"]
                 [hiccup "1.0.5"]
                 [ring/ring-json "0.3.1"]
                 [clj-http "1.0.1"]
                 [tentacles "0.3.0"]
                 [com.taoensso/timbre "3.3.1"]
                 [com.novemberain/monger "2.0.1"]
                 [ancient-clj "0.3.2"]
                 [com.draines/postal "1.11.3"]
                 [version-clj "0.1.1"]
                 [clojurewerkz/quartzite "2.0.0"]
                 [prismatic/schema "0.3.3"]
                 [com.stuartsierra/component "0.2.2"]
                 [me.raynes/fs "1.4.6"]
                 [com.googlecode.streamflyer/streamflyer-core "1.1.3"]
                 [commons-io "2.4"]

                 ; ClojureScript
                 [org.clojure/clojurescript "0.0-2665"]
                 [jayq "2.5.2"]
                 [org.om/om "0.8.0"]]

  :plugins [[lein-cljsbuild "1.0.4"]]

  :main hatnik.system
  :source-paths ["src/clj" "target/gen/clj"]
  :test-paths ["test/clj"]

  :test-selectors {:selenium :selenium
                   :unit (complement :selenium)}

  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]
                        [com.cemerick/piggieback "0.1.5"]
                        [org.seleniumhq.selenium/selenium-java "2.44.0"]
                        [org.seleniumhq.selenium/selenium-remote-driver "2.44.0"]
                        [org.seleniumhq.selenium/selenium-server "2.44.0"]]

         :plugins [[jonase/eastwood "0.2.1"]
                   [com.keminglabs/cljx "0.5.0" :exclusions [org.clojure/clojure]]]

         :clean-targets ^{:protect false} ["resources/public/gen"]

         :cljsbuild
         {:builds
          [{:source-paths ["src/cljs" "dev/cljs" "target/gen/cljs"]
            :compiler
            {:output-to "resources/public/gen/js/hatnik.js"
             :output-dir "resources/public/gen/out"
             :optimizations :none
             :pretty-print true
             :preamble ["react/react.js"]}}]
          }
         :cljx {:builds [{:source-paths ["src/cljx"]
                 :output-path "target/gen/clj"
                 :rules :clj}

                {:source-paths ["src/cljx"]
                 :output-path "target/gen/cljs"
                 :rules :cljs}]}}
   :release
   {:cljsbuild
    {:builds
     [{:source-paths ["src/cljs" "target/gen/cljs"]
       :compiler
       {:output-to "resources/public/gen/js/hatnik.js"
        :optimizations :advanced
        :externs ["externs/jquery-1.9.js"
                  "externs/hatnik.js"]
        :preamble ["react/react.min.js"]
        :pretty-print false}}]
     }}})
