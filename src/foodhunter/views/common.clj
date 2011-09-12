(ns foodhunter.views.common
  (:require [foodhunter.model.user :as user] 
            [noir.session :as session]
            [noir.validation :as vali]
            [noir.response :as resp])  
  (:use noir.core
        hiccup.core
        hiccup.page-helpers
        hiccup.form-helpers))

(def *unknown-user* "unknown user")

(defn build-form-table [& items]
  [:table
   (into [:tbody]
         (map (fn [[name field]]
                [:tr [:td name] [:td field]]) 
              items))])

(defn render-li [item]
  [:li.menuitem item])

(defn build-menu [& items]
  [:div#header
   [:duv#logo
    [:h1 (link-to "/" "Food Hunter")]]   
   [:div#menu
    (into [:ul] (map render-li items))]])

(defpartial header []
  (build-menu 
    (link-to "/" "Home")
    (link-to "/profile" (session/get :username))
    (link-to "/logout" "Logout")))

(defpartial headerbg []           
  [:div#headerbg
    [:p.headerText "\"Something clever" [:br] "should go here :)\""]])

(defpartial sidebar [header & items]
  [:div#sidebar 
   [:ul
    [:li [:h2 header] 
     (into [:ul] (map render-li items))]]
   [:div {:style "clear: both;"}]])

(defpartial footer []
  [:div#footer
       [:p#legal "c 2011 Food Hunter. All Rights Reserved."]
       [:p#links (link-to "/contact" "contact")]])

(defpartial layout [& content]
  (html5
    [:head
     [:title "Food Hunter"]
     (include-css "/css/default.css"
                  "/css/jquery.rating.css")
     (include-js "/js/jquery.js"
                 "/js/jquery.rating.js"                 
                 "/js/json2.js"
                 "/js/pure.js"
                 "/js/site.js")]
    [:body
     [:div#main content]]))

(defpartial page [& page-content]  
  (if (session/get :username)
    (layout 
      (header)   
      (headerbg)
      [:div#page [:div#content page-content]
       (sidebar "Suggestions" (link-to "/" "Some restaurant suggestion"))]      
      (footer))
    (render "/login")))


(defpage "/logout" []
  (session/clear!)
  (resp/redirect "/"))


(defpartial user-fields []
            (vali/on-error :username *unknown-user*)
            (text-field {:placeholder "Username"} :username)
            (password-field {:placeholder "Password"} :password))


(defpage "/login" []
  (layout
    (build-menu
      (link-to "/register" "register"))
    (headerbg)
    [:div#page     
     (form-to [:post "/login"]             
              (user-fields)
              (submit-button {:class "submit"} "submit"))]
    (footer)))

(defn validate-registration [email pass]
  (and (vali/is-email? email) (not-empty pass)))

(defpage "/register" []  
  (render [:post "/register"]))

(defpage [:post "/register"] 
  {firstname :firstname, lastname :lastname, email :email, password :password}
  (layout
    (build-menu)
    (headerbg)
    [:div#page
     (form-to [:post "/process-registration"]
              (build-form-table
                ["first name" (text-field "firstname" firstname)]
                ["last name" (text-field "lastname" lastname)]
                ["email" (text-field "email" email)]
                ["password" (password-field "password" password)])              
              (submit-button {:class "submit"} "submit"))]
    (footer)))

(defpage [:post "/process-registration"] 
  {firstname :firstname, lastname :lastname, email :email, password :password}
  (if (validate-registration email password) 
    (do
      (user/add-user firstname lastname email password)
      (session/put! :username email)
      (layout
        (build-menu (link-to "/" "to main page"))
        (headerbg)
        [:div#page 
         [:h1 "Registration info"
          [:ul
           [:li firstname]
           [:li lastname]
           [:li email]
           [:li password]]]]
        (footer)))
    (render [:post "/register"] {:firstname firstname, :lastname lastname, :email email, :password password})))

(defpage [:post "/login"] {username :username, password :password}  
  (let [user (.trim username)] 
    (if (user/validate-user user password)
      (do 
        (session/put! :username user)
        (resp/redirect "/"))
      (render "/"))))

(defpage "/contact" []
  (layout
    (build-menu (link-to "/" "to main page"))
    (headerbg)
    [:div#page
     [:h1.title "Contact information"]
     [:p.entry "Some contact info here"]]
    (footer)))
