(ns need-for-speed.core
    (:require
      [goog.string :as gstring]
      [reagent.core :as r]
      [reagent.dom :as d]))

;; -------------------------
;; Models

(defonce cars
  [{:name "A" :consumption 3.0}
   {:name "B" :consumption 3.5}
   {:name "C" :consumption 4.0}])

;; -------------------------
;; States

(defonce form
  (r/atom {:car (:name (first cars))
           :distance 100
           :speed-x 60
           :speed-y 120}))

(defn set-car [new-car]
  (swap! form assoc :car new-car))

(defn set-number-input [key event]
  (let [value (-> event .-target .-value int)]
    (when (< 0 value 10000) ;; TODO: Prolly replace with validation messages
      (swap! form assoc key value))))

;; -------------------------
;; Views

(defn car-checkbox [{:keys [name consumption]}]
  [:label.block
   [:input {:type "radio"
            :name "Car"
            :value name
            :checked (= name (:car @form))
            :on-change #(set-car name)}]
   (gstring/format "Car %s (%.1f litres/100km at 1km/h)" name consumption)])

(defn car-checkboxes []
  [:fieldset
   [:legend "Choose your car"]
   (for [car cars]
     ^{:key (:name car)} [car-checkbox car])])

(defn number-input [label key]
  [:label.block
   [:input.border.border-gray-400 {:type "number"
                                   :name label
                                   :value (@form key)
                                   :on-change #(set-number-input key %)}]
   label])

(defn app []
  [:div.font-mono.max-w-xl.mx-auto.p-6
   [:h1.text-2xl "Need for Speed"]
   [car-checkboxes]
   [number-input "Distance" :distance]
   [number-input "Speed X" :speed-x]
   [number-input "Speed Y" :speed-y]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [app] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
