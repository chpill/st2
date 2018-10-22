(ns st2.core
  #?(:cljs (:require-macros [st2.core :refer [ss]]
                            [net.cgrand.macrovich :as macrovich]))
  #?(:clj (:require [net.cgrand.macrovich :as macrovich]
                    [clojure.string :as str]

                    [cljs.build.api]

                    [cljs.analyzer :as analyzer]
                    [cljs.analyzer.api :as analyzer-api]
                    [cljs.compiler.api :as compiler]
                    [clojure.java.io :as io]

                    ))

  #?(:clj (:import (cljs.tagged_literals JSValue))))





(macrovich/deftime

  ;; From Roman01la https://gist.github.com/roman01la/98d23f56468e266d86314aadabb56f12

  ;; Sablono's stuff
  ;; Converting Clojure data into ClojureScript (JS)
  ;; ====================================================
  (defprotocol IJSValue
    (to-js [x]))

  (defn- to-js-map [m]
    (JSValue. (into {} (map (fn [[k v]] [k (to-js v)])) m)))

  (extend-protocol IJSValue
    clojure.lang.Keyword
    (to-js [x]
      (if (qualified-keyword? x)
        (str (namespace x) "/" (name x))
        (name x)))
    clojure.lang.PersistentArrayMap
    (to-js [x]
      (to-js-map x))
    clojure.lang.PersistentHashMap
    (to-js [x]
      (to-js-map x))
    clojure.lang.PersistentVector
    (to-js [x]
      (JSValue. (mapv to-js x)))
    Object
    (to-js [x]
      x)
    nil
    (to-js [_]
      nil))

  ;; =========================================

  (def alphabet "abcdefghijklmnopqrstuvwxyz")

  ;; TODO augment the number of possible chars
  ;; (def class-chars (concat (map str alphabet)
  ;;                          (map str/upper-case alphabet)))

  ;; (def card (count class-chars))

  ;; Ugly but easy.
  ;; We cannot use the radix feature of `.toString` above 36
  ;; TODO do this better so that we case can also use upper case
  ;; chars in class names. Maybe also digits after the first letter?;
  (defn make-class-name [n]
    (->> (Integer/toString n 26)
         (map #(Integer/parseInt (str %) 26))
         (map #(nth alphabet %))
         (apply str)))


  (def ^:dynamic *collect-styles?* false)
  (def ^:dynamic **styles-accumulator* (atom {}))


  (defn by-file-then-by-line [a b]
    (let [file-comparison (compare (:file a)
                                   (:file b))]
      (if-not (zero? file-comparison)
        file-comparison
        (compare (:line a)
                 (:line b)))))


  (defn add-key [acc k call-site-data]
    (if (get acc k)
      (update-in acc [k :call-sites] conj call-site-data)
      (assoc acc k {:class-name (make-class-name (count acc))
                    :call-sites (sorted-set-by by-file-then-by-line
                                               call-site-data)})))

  (defn aggregate [key form-meta]
    (swap! **styles-accumulator*
           add-key
           key
           (select-keys form-meta [:file :line])))

  (defn extract-styles-from-source []
    (println "Expansion of macros and extraction of styles...")
    (let [*gettext-keys (atom {})
          relative-path "src"
          absolute-path (str (System/getProperty "user.Dir")
                             "/src/")]
      (binding [*collect-styles?*     true
                **styles-accumulator* *gettext-keys]

        (let [empty-compiler-state (analyzer-api/empty-state)]
          (doseq [cljs-file (compiler/cljs-files-in (io/file "src"))]
            (analyzer/with-warning-handlers
              [] ;; no handlers!
              (analyzer-api/analyze-file empty-compiler-state
                                         cljs-file
                                         {:cache-analysis false})))))

      (println "Extraction done.")

      @*gettext-keys))

  (comment

    (clojure.pprint/pprint (extract-styles-from-source))

    )


  ;; ================================================

  (defn kebab->camel [^String method-name]
    (str/replace
     method-name
     #"-(\w)"
     #(str/upper-case (second %1))))

  (defn kebab-k->camel-s [k]
    (-> k name kebab->camel))

  (defn clj-css->rn-css [styles]
    (into {}
          (map (fn [[k v]]
                 [(kebab-k->camel-s k) v]))
          styles))

  (defmacro ss [style-statements]

    (let [styles-value
          (-> style-statements
              clojure.core/eval
              ;; TODO tell the user nicely
              ;; (try
              ;;   ;; (catch clojure.lang.Compiler$CompilerException e
              ;;   (catch Exception e
              ;;     ;; Tell the user that her style declaration is not statically defined and
              ;;     ;; cannot be resolved
              ;;     (throw (Exception. "style declaration is not statically defined and cannot be resolved"))))

              ;; TODO move this in output namespace
              ;; clj-css->rn-css
              ;; to-js-map
              )]

      (assert (map? styles-value))

      (doseq [style-statement styles-value]
        (aggregate style-statement
                   (meta &form)))


      styles-value))


  (defmacro plop [form]
    (macrovich/case
        :clj (hash form)
        :cljs (hash form)))

  )






(comment

  (ss {:a 3 :d 4})

  (do
    (def a 1)
    (def b "#000000")

    (ss {:a a :b b}))

  ;; This cannot work with eval, this is dynamic!
  (let [a 1 b "#000000"]
    (ss {:a a :b b}))

  (plop (assoc {:a 1} :b 2))
  
  )
