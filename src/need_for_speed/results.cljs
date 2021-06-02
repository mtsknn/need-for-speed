(ns need-for-speed.results
  (:require
   [goog.string :as gstring]
   [need-for-speed.data :as data]
   [need-for-speed.form :as form]))

(defn fuel-consumption-multiplier [speed]
  (Math/pow 1.009 (dec speed)))

(defn fuel-per-100km [base-consumption speed]
  (* base-consumption (fuel-consumption-multiplier speed)))

(defn total-fuel [consumption distance]
  (* consumption (/ distance 100)))

(defn total-time [distance speed]
  (/ distance speed))

(def format-float
  (let [formatter (js/Intl.NumberFormat. "en" #js {:minimumFractionDigits 2
                                                   :maximumFractionDigits 2})]
    (fn [number]
      (.format formatter number))))

(def format-int
  (let [formatter (js/Intl.NumberFormat. "en" #js {:maximumFractionDigits 0})]
    (fn [number]
      (.format formatter number))))

(def diff-floats
  (let [formatter (js/Intl.NumberFormat. "en" #js {:signDisplay "always"
                                                   :minimumFractionDigits 2
                                                   :maximumFractionDigits 2})]
    (fn [x y]
      (let [number (- x y)]
        (if (= 0 number)
          "±0"
          (.format formatter number))))))

(defn view []
  (let [{:keys [car distance speed-x speed-y]} @form/state
        base-fuel (-> car data/cars :consumption)
        fuel-x-per-100km (fuel-per-100km base-fuel speed-x)
        fuel-y-per-100km (fuel-per-100km base-fuel speed-y)
        fuel-x (total-fuel fuel-x-per-100km distance)
        fuel-y (total-fuel fuel-y-per-100km distance)
        time-x (total-time distance speed-x)
        time-y (total-time distance speed-y)]
    [:pre.mt-4
     [:div
      (gstring/format "Fuel per 100km at %s km/h: %s liters (%s)"
                      (format-int speed-x)
                      (format-float fuel-x-per-100km)
                      (diff-floats fuel-x-per-100km fuel-y-per-100km))]
     [:div
      (gstring/format "Total fuel     at %s km/h: %s liters (%s)"
                      (format-int speed-x)
                      (format-float fuel-x)
                      (diff-floats fuel-x fuel-y))]
     [:div
      (gstring/format "Total time     at %s km/h: %s hours (%s)"
                      (format-int speed-x)
                      (format-float time-x)
                      (diff-floats time-x time-y))]
     [:hr.my-4]
     [:div
      (gstring/format "Fuel per 100km at %s km/h: %s liters (%s)"
                      (format-int speed-y)
                      (format-float fuel-y-per-100km)
                      (diff-floats fuel-y-per-100km fuel-x-per-100km))]
     [:div
      (gstring/format "Total fuel     at %s km/h: %s liters (%s)"
                      (format-int speed-y)
                      (format-float fuel-y)
                      (diff-floats fuel-y fuel-x))]
     [:div
      (gstring/format "Total time     at %s km/h: %s hours (%s)"
                      (format-int speed-y)
                      (format-float time-y)

                      (diff-floats time-y time-x))]]))
