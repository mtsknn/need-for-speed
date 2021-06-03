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

(defn format-time [time]
  (let [hours (Math/trunc time)
        minutes (* 60 (mod time 1))
        show-hours? (not= 0 hours)
        show-minutes? (or (= 0 hours) (not= 0 minutes))]
    (str (when show-hours?
               (str (format-int hours) " h"))
         (when (and show-hours? show-minutes?)
               " ")
         (when show-minutes?
               (str (format-int minutes) " min")))))

(def diff-floats
  (let [formatter (js/Intl.NumberFormat. "en" #js {:signDisplay "always"
                                                   :minimumFractionDigits 2
                                                   :maximumFractionDigits 2})]
    (fn [x y]
      (let [number (- x y)]
        (if (= 0 number)
          "±0"
          (.format formatter number))))))

(defn diff-times [x y]
  (let [number (- x y)]
    (if (= 0 number)
      "±0"
      (str (if (< number 0) "-" "+") (format-time (Math/abs number))))))

(defn report-fuel-per-100km [speed fuel-per-100km other-fuel-per-100km]
  (gstring/format "Fuel/100km at %s km/h: %s liter%s (%s)"
                  (format-int speed)
                  (format-float fuel-per-100km)
                  (if (= 1 fuel-per-100km) "" "s")
                  (diff-floats fuel-per-100km other-fuel-per-100km)))

(defn report-total-fuel [speed fuel other-fuel]
  (gstring/format "Total fuel at %s km/h: %s liter%s (%s)"
                  (format-int speed)
                  (format-float fuel)
                  (if (= 1 fuel) "" "s")
                  (diff-floats fuel other-fuel)))

(defn report-total-time [speed time other-time]
  (gstring/format "Total time at %s km/h: %s (%s)"
                  (format-int speed)
                  (format-time time)
                  (diff-times time other-time)))

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
     [:div (report-fuel-per-100km speed-x fuel-x-per-100km fuel-y-per-100km)]
     [:div (report-total-fuel speed-x fuel-x fuel-y)]
     [:div (report-total-time speed-x time-x time-y)]
     [:hr.my-4]
     [:div (report-fuel-per-100km speed-y fuel-y-per-100km fuel-x-per-100km)]
     [:div (report-total-fuel speed-y fuel-y fuel-x)]
     [:div (report-total-time speed-y time-y time-x)]]))
