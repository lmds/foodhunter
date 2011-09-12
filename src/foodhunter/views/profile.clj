(ns foodhunter.views.profile
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

(defn render-profile [username]
  (let [{firstname :firstname, lastname :lastname, password :password} (user/get-profile username)] 
    [:table
     [:tbody
      [:tr [:td "First name"] [:td firstname]]
      [:tr [:td "Last name"] [:td lastname]]
      [:tr [:td "Password"] [:td password]]]]))

(defpage "/profile" []
  (let [username (session/get :username)] 
    (common/page
      [:h1 username]
      (render-profile username))))

