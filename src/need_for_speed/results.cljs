(ns need-for-speed.results
    (:require
      [goog.string :as gstring]
      [need-for-speed.data :as data]
      [need-for-speed.form :as form]))

(defn view []
  (let [base-fuel (-> (@form/state :car) data/cars :consumption)
        distance (@form/state :distance)
        speed-x (@form/state :speed-x)
        speed-x-multiplier (Math/pow 1.009 (dec speed-x))
        speed-y (@form/state :speed-y)
        speed-y-multiplier (Math/pow 1.009 (dec speed-y))
        fuel-x-per-100 (* (/ distance 100) base-fuel speed-x-multiplier)
        fuel-y-per-100 (* (/ distance 100) base-fuel speed-y-multiplier)
        time-x (/ distance speed-x)
        time-y (/ distance speed-y)]
    [:div
     [:div (gstring/format "Fuel at speed X: %.2f litres" fuel-x-per-100)]
     [:div (gstring/format "Fuel at speed Y: %.2f litres" fuel-y-per-100)]
     [:div (gstring/format "Time at speed X: %.2f hours" time-x)]
     [:div (gstring/format "Time at speed Y: %.2f hours" time-y)]]))
