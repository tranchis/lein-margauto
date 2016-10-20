(ns leiningen.margauto
  "Automatically update Marginalia-generated documentation.

  Monitor Clojure and ClojureScript files in specified directories,
  updating at intervals when a source file changes."
  (:require [leiningen.marg :as lm]
            [clojure.tools.cli :as cli]))

(def clojure-extensions
  "Set of acceptable extensions for Clojure/ClojureScript"
  #{".clj" ".cljs" ".cljc"})

(defn ends-with-one-of
  "The given string `s` ends with one of the strings in `coll`"
  [coll s]
  (letfn [(ends-with-this [suffix]
            (.endsWith s suffix))]
    (some ends-with-this coll)))

(defn source-files-seq
  "Return Clojure/ClojureScript files in the directory trees in `dirs`"
  [dirs]
  (map str
       (filter
         #(and
           (.isFile %1)
           (ends-with-one-of clojure-extensions (str %1)))
         (mapcat file-seq (map (fn [s] (java.io.File. s)) dirs)))))

(defn mtime
  "Extract the last-modified time for this file"
  [f]
  (.lastModified (java.io.File. f)))

(defn take-directory-snapshot
  "Grab a snapshot of the directory now, to see what's changed
   in the future."
  [dirs]
  (apply
    str
    (map
      (fn [f]
        (format "%s:%s\n" f (mtime f)))
      (source-files-seq dirs))))

(def target-directory
  "Where are we putting the output document?"
  (ref "docs/"))

(defn eval-in-project
  "Support eval-in-project in both Leiningen 1.x and 2.x."
  [project form init]
  (let [[eip two?] (or (try (require 'leiningen.core.eval)
                            [(resolve 'leiningen.core.eval/eval-in-project)
                             true]
                            (catch java.io.FileNotFoundException _))
                       (try (require 'leiningen.compile)
                            [(resolve 'leiningen.compile/eval-in-project)]
                            (catch java.io.FileNotFoundException _)))]
    (if two?
      (eip project form init)
      (eip project form nil nil init))))

(def dep
  "Dependencies to add to project in order to run *Marginalia*"
  ['lein-marginalia "0.9.0"])

(defn- add-marg-dep
  "Temporarily add a dependency to *Marginalia* to the project"
  [project]
  ;; Leiningen 2 is a bit smarter about only conjing it in if it
  ;; doesn't already exist and warning the user.
  (if-let [conj-dependency (resolve 'leiningen.core.project/conj-dependency)]
    (conj-dependency project dep)
    (update-in project [:dependencies] conj dep)))

(defn margauto
  "Watch for changes and rebuild the documentation automatically."
  [project & args]
  (let [src-dirs   (get-in project [:margauto :src-dirs] ["src"])
        pause-time (get-in project [:margauto :sleep-time] 1000)]
    (dosync
      (alter target-directory (fn [_] (get-in project [:margauto :target-dir] "docs"))))
    (eval-in-project (add-marg-dep project)
                     (leiningen.marg/marg project "-d" @target-directory)
                     (loop [before (take-directory-snapshot src-dirs)
                            after  before]
                       (if-not (= after before)
                         (leiningen.marg/marg project "-d" @target-directory))
                       (Thread/sleep pause-time)
                       (recur after (take-directory-snapshot src-dirs))))))


