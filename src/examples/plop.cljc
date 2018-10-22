(ns examples.plop
  (:require [st2.core :as st2]))

(def plop-color "papayawhip")

(def container {:flex 1
                :background-color plop-color})


(st2/ss container)
