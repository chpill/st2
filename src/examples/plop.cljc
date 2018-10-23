(ns examples.plop
  (:require [st2.core :as st2]
            [examples.styles :as styles]))

(def plop-color "papayawhip")

(def container {:flex 1
                :background-color plop-color})


(st2/ss (assoc container
               :color styles/c-black))


(st2/ss (into styles/black-box
              container))

(def gutter "8px")

(st2/ss {:flex 1 :margin gutter})
