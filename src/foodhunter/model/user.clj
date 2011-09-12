(ns foodhunter.model.user)

;;check how often the user likes to eat at a place, factor into suggestions

(def *users*
  (atom 
    {"john.doe@gmail.com"
     {:info 
      {:firstname "John", :lastname "Doe", :password "foo"}
      
      :restaurants
      {"Salad King"    {:rating 5 :style "South Asian"}
       "Matagali"      {:rating 4 :style "Indian"}
       "Sushi & Bento" {:rating 5 :style "Japanese"}}}}))

(defn validate-user [username password]    
  (= password (:password (:info (get @*users* username)))))

(defn add-user [firstname lastname email password]  
  (swap! *users*
         (fn [users]
           (assoc users email {:info {:firstname firstname, :lastname lastname, :password password}}))))

(defn get-profile [userid]
  (:info (get @*users* userid)))

(defn get-restaurants [user]  
  (:restaurants (get @*users* user)))

(defn add-restuarant [user restaurant rating style]  
  (swap! *users*
         (fn [users]
           (update-in users [user :restaurants] 
                      (fn [restaurants] 
                        (assoc restaurants restaurant {:rating rating :style style}))))))

(defn update-rating [user name rating]  
  (swap! *users*
         (fn [users] 
           (update-in users [user :restaurants name :rating] (fn [_] rating)))))