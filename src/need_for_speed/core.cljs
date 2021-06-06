(ns need-for-speed.core
  (:require
   [goog.string :as gstring]
   [need-for-speed.form :as form]
   [need-for-speed.results :as results]
   [reagent.dom :as d]))

(defn app []
  [:div.max-w-sm.mx-auto.my-4.p-6.sm:max-w-lg.md:max-w-3xl.lg:my-8
   [:h1.font-black.mb-6.text-3xl "Need for Speed"]
   [:p.text-lg "...or is there? Check how different driving speeds affect fuel consumption and travel time."]
   [:hr.my-8]
   [form/view]
   [:hr.my-8]
   [results/view]
   [:hr.my-8]
   [:p
    [:a.text-gray-500.underline.text-sm.hover:text-indigo-500
     {:class "hover:text-indigo-500 focus:(outline-none ring(& indigo-500 offset-2))"
      :href "https://github.com/mtsknn/need-for-speed"}
     "Check out the project on GitHub"
     (gstring/unescapeEntities " &rarr;")]]])

(defn mount-root []
  (d/render [app] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
