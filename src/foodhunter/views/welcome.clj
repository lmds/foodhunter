(ns foodhunter.views.welcome
  (:require [foodhunter.views.common :as common]
            [foodhunter.model.user :as user]
            [noir.content.pages :as pages]
            [noir.session :as session]
            [noir.validation :as vali]
            [noir.response :as resp])  
  (:use noir.core
        hiccup.core
        hiccup.page-helpers
        hiccup.form-helpers))



(defn render-rating [name rating autosumbit?]  
  (into [:div {:class "Clear"}] 
        (map (fn [v] 
               (let [attrs {:name name 
                            :type "radio" 
                            :class (if autosumbit? "auto-submit-star" "star")
                            :value v}]
                 
                 [:input (if (= v rating)
                           (assoc attrs :checked "checked")
                           attrs)]))
             (range 1 6))))

(defn render-restaurants [restaurants]
  [:table
   [:tbody
    [:tr [:th "Name"] [:th "Rating"] [:th "Style"]]
    (for [[name {id :id, rating :rating, style :style}] restaurants]
      [:tr 
       [:td name] 
       [:td (render-rating name rating true)] 
       [:td style]])]])

(defpartial add-restaurant []
  (form-to [:post "/add-restaurant"]
           (common/build-form-table 
             ["name" (text-field "name")]             
             ["style" (text-field "style")]
             ["rating" (render-rating "rating" 0 false)])
           (submit-button {:class "submit"} "add")))

(defpage "/" []
  (common/page   
    [:h1.title "Your favorites"]
    [:div.post
     [:div.entry
      (render-restaurants (user/get-restaurants (session/get :username)))
      [:h3 "add a restaurant"]
      (add-restaurant)]]))

(defpage [:post "/submit-rating"] {name :name rating :rating}
  (user/update-rating (session/get :username) name (Integer/parseInt rating))  
  (resp/json "ok"))

(defpage [:post "/add-restaurant"] {name :name, rating :rating, style :style}
  (user/add-restuarant (session/get :username) name (Integer/parseInt rating) style)
  (resp/redirect "/"))
