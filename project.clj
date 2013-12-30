(defproject com.github.kyleburton/lein-margauto "1.0.0"
  :description "Leiningen plugin: Autobuilder and simple server for Marginalia documentation."
  :local-repo-classpath true
  :dev-dependencies [[swank-clojure "1.4.0-SNAPSHOT"]]
  :plugins [[no-man-is-an-island/lein-eclipse "2.0.0"]]
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [noir "1.3.0"]
                 [leiningen "2.3.4"]
                 [lein-marginalia "0.6.0"]])
