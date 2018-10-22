(ns user
  (:require [figwheel.main]))

(def cljs-options {:main "examples.core"
                   :closure-defines {'goog.DEBUG true}})

(defn go! []
  (figwheel.main/start {:watch-dirs ["src/"]
                        :log-file "figwheel-main.log"}
                       {:id "dev" :options cljs-options}))
