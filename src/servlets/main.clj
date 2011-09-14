(ns servlets.main
  (:gen-class :extends javax.servlet.http.HttpServlet)
  (:require [compojure.route :as route]
            [noir.server :as server]
            [foodhunter.views.welcome]
            [foodhunter.views.common]
            [foodhunter.views.profile])
  (:use ring.util.servlet
        compojure.core
        [hiccup.middleware :only (wrap-base-url)]))


(def app
  (do
    (server/load-views-ns 'foodhuner.views)
    (-> (server/gen-handler {:mode :prod, :ns 'foodhunter.views})
        (wrap-base-url))))