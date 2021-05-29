(ns need-for-speed.core
    (:require
      [goog.string :as gstring]
      [reagent.core :as r]
      [reagent.dom :as d]))

;; -------------------------
;; Models

(defonce cars
  {:A {:consumption 3.0}
   :B {:consumption 3.5}
   :C {:consumption 4.0}})

;; -------------------------
;; States

(defonce form
  (r/atom {:car (-> cars first key)
           :distance 200
           :speed-x 80
           :speed-y 120}))

(defn set-car [new-car]
  (swap! form assoc :car new-car))

(defn set-number-input [key event]
  (let [value (-> event .-target .-value int)]
    (when (< 0 value 10000) ;; TODO: Prolly replace with validation messages
      (swap! form assoc key value))))

;; -------------------------
;; Views

(defn car-checkbox [[key {:keys [:consumption]}]]
  [:label.block
   [:input {:type "radio"
            :name "Car"
            :value key
            :checked (= key (@form :car))
            :on-change #(set-car key)}]
   (gstring/format "Car %s (%.1f litres/100 km at 1 km/h)" (name key) consumption)])

(defn car-checkboxes []
  [:fieldset
   [:legend "Choose your car"]
   (for [car cars]
     ^{:key (key car)} [car-checkbox car])])

(defn number-input [label key]
  [:label.block
   [:input.border.border-gray-400 {:type "number"
                                   :name label
                                   :value (@form key)
                                   :on-change #(set-number-input key %)}]
   label])

(defn results []
  (let [base-fuel (-> (@form :car) cars :consumption)
        distance (@form :distance)
        speed-x (@form :speed-x)
        speed-x-multiplier (Math/pow 1.009 (dec speed-x))
        speed-y (@form :speed-y)
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

(defn app []
  [:div.font-mono.max-w-xl.mx-auto.p-6
   [:h1.text-2xl "Need for Speed"]
   [car-checkboxes]
   [number-input "Distance (km)" :distance]
   [number-input "Speed X (km/h)" :speed-x]
   [number-input "Speed Y (km/h)" :speed-y]
   [results]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [app] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
