(defproject com.github.luskwater/lein-margauto "1.0.16"
  :description "Leiningen plugin: Autobuilder and simple server for Marginalia documentation."
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[lein-marginalia "0.9.0"]]
  :repositories [["releases" {:url "https://clojars.org/repo"
                              :creds :gpg}]]
  :profiles {:provided {:dependencies [[org.clojure/clojure "1.8.0"]]}})
