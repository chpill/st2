(ns st2.core
  #?(:cljs (:require-macros [st2.core]
                            [net.cgrand.macrovich :as macrovich]))
  #?(:clj (:require [net.cgrand.macrovich :as macrovich]
                    [clojure.string :as str])))




(macrovich/deftime

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
    (->> style-statements
         eval
         #_(try
             (catch clojure.lang.Compiler$CompilerException e
               ;; Tell the user that her style declaration is not statically defined and
               ;; cannot be resolved
               "style declaration is not statically defined and cannot be resolved"))

         clj-css->rn-css

         ;;(list 'js*)

         )))
