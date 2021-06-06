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

(defn almost-zero? [value]
  (< -0.005 value 0.005))

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
        show-hours? (not (almost-zero? hours))
        show-minutes? (or (not show-hours?) (not (almost-zero? minutes)))]
    (str (when show-hours?
               (str (format-int hours) (gstring/unescapeEntities "&nbsp;h")))
         (when (and show-hours? show-minutes?)
               " ")
         (when show-minutes?
               (str (format-int minutes) (gstring/unescapeEntities "&nbsp;min"))))))

(def diff-floats
  (let [formatter (js/Intl.NumberFormat. "en" #js {:signDisplay "always"
                                                   :minimumFractionDigits 2
                                                   :maximumFractionDigits 2})]
    (fn [x y]
      (let [number (- x y)]
        (if (almost-zero? number)
          "±0"
          (.format formatter number))))))

(defn diff-times [x y]
  (let [number (- x y)]
    (if (almost-zero? number)
      "±0"
      (str (if (neg? number) "-" "+") (format-time (Math/abs number))))))

(defn arrow [direction color]
  {:pre (some #{direction} '(:up :down))}
  [:svg {:class ["-ml-1 mr-0.5 flex-shrink-0 self-center h-5 w-5" color]
         :viewBox "0 0 20 20"
         :fill "currentColor"
         :aria-hidden "true"}
   [:path {:d (case direction
                :up "M5.293 9.707a1 1 0 010-1.414l4-4a1 1 0 011.414 0l4 4a1 1 0 01-1.414 1.414L11 7.414V15a1 1 0 11-2 0V7.414L6.707 9.707a1 1 0 01-1.414 0z"
                :down "M14.707 10.293a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 111.414-1.414L9 12.586V5a1 1 0 012 0v7.586l2.293-2.293a1 1 0 011.414 0z")
           :clip-rule "evenodd"
           :fill-rule "evenodd"}]])

(defn heading [speed]
  [:h3.text-lg.leading-6.font-medium.text-gray-900.mt-8
   (str "At " speed " km/h")])

(defn diff-badge [{:keys [value other-value formatter good?]}]
  (let [diff (- value other-value)]
    [:div {:class ["inline-flex items-baseline"
                   "mt-2 px-2.5 py-0.5 rounded-full"
                   "font-medium text-sm"
                   "sm:justify-between"
                   (if (almost-zero? diff)
                     "bg-yellow-200 text-yellow-800"
                     (if good? "bg-green-100 text-green-800" "bg-red-100 text-red-800"))]}
     (when-not (almost-zero? diff)
       [arrow (if (pos? diff) :up :down) (if good? "text-green-500" "text-red-500")])
     (formatter value other-value)]))

(defn dl [& children]
  [:dl {:class ["grid(& cols-1) mt-5"
                "bg-white shadow rounded-lg"
                "divide(y gray-200)"
                "md:(grid-cols-3 divide(y-0 x))"]}
   (into [:<>] children)])

(defn dt [{:keys [title value unit diff]}]
  [:div.px-4.py-5.sm:p-6
   [:dt.text-gray-900 title]
   [:dd.mt-1 {:class "sm:(flex justify-between items-baseline) md:block"}
    [:div.flex.items-baseline.text-2xl.font-semibold.text-indigo-600.md:block
     value
     (and unit [:span.ml-2.text-sm.font-medium.text-gray-500 unit])]
    (and diff [diff-badge diff])]])

(defn report-fuel-per-100km [value other-value]
  [dt {:title "Fuel per 100km"
       :value (format-float value)
       :unit (str "liter" (when (not= 1 value) "s"))
       :diff {:value value
              :other-value other-value
              :formatter diff-floats
              :good? (< value other-value)}}])

(defn report-total-fuel [value other-value]
  [dt {:title "Total fuel"
       :value (format-float value)
       :unit (str "liter" (when (not= 1 value) "s"))
       :diff {:value value
              :other-value other-value
              :formatter diff-floats
              :good? (< value other-value)}}])

(defn report-total-time [value other-value]
  [dt {:title "Total time"
       :value (format-time value)
       :diff {:value value
              :other-value other-value
              :formatter diff-times
              :good? (< value other-value)}}])

(defn view []
  (let [{:keys [car distance speed-x speed-y]} @form/state
        base-fuel (-> car data/cars :consumption)
        fuel-x-per-100km (fuel-per-100km base-fuel speed-x)
        fuel-y-per-100km (fuel-per-100km base-fuel speed-y)
        fuel-x (total-fuel fuel-x-per-100km distance)
        fuel-y (total-fuel fuel-y-per-100km distance)
        time-x (total-time distance speed-x)
        time-y (total-time distance speed-y)]
    [:div.mt-4
     [heading speed-x]
     [dl
      [report-fuel-per-100km fuel-x-per-100km fuel-y-per-100km]
      [report-total-fuel fuel-x fuel-y]
      [report-total-time time-x time-y]]
     [heading speed-y]
     [dl
      [report-fuel-per-100km fuel-y-per-100km fuel-x-per-100km]
      [report-total-fuel fuel-y fuel-x]
      [report-total-time time-y time-x]]]))
