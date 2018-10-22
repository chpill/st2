(ns examples.core
  (:require [st2.core :refer [ss plop]]
            [examples.styles :as styles]
            [net.cgrand.macrovich :as macrovich]))




(js/console.log "Example core ns evaluating here")


(macrovich/deftime
  (def black styles/black-box)

  )


;; (ss {:padding "10px" :color "papayawhip"})


;; (plop (assoc {:a 1} :b 2))

;; (ss black)




;; (ss (assoc {:border-radius "5px"}
;;            :border-width "1px"))
