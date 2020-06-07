(ns example-component
  (:require [com.stuartsierra.component :as component]
            [monger.core :as mg]
            [monger.collection :as mc]
            [ring.adapter.jetty :refer [run-jetty]]

            [example-seed :as seed]))

;;
;; Todo o ciclo de vida do banco de dados
;; fica encapsulado em um componente.
;;
(defrecord Database [uri database connection]
  component/Lifecycle

  (start [component]
    (let [{:keys [conn db]} (mg/connect-via-uri uri)]
      ;; Inicializa alguma informacao no banco para
      ;; podermos testar a aplicacao.
      (seed/seed-db! db)
      (assoc component :database db
                       ::connection conn)))

  (stop [component]
    (-> component ::connection mg/disconnect)
    component))

;;
;; O construtor tem que ser uma funcao pura
;;
(defn new-database [uri]
  (map->Database {:uri uri}))

;;
;; O web server pode seguir o mesmo protocolo
;; e principios que o banco de dados.
;;
(defrecord WebServer [port app]
  component/Lifecycle

  (start [component]
    (assoc component
      ::jetty
      (run-jetty (-> component :app :handler)
                 {:port port})))

  (stop [component]
    (-> component ::jetty .close)
    component))

(defn new-web-server [port]
  (component/using
    (map->WebServer {:port port})
    ;; Declaramos explicitamente a dependencia
    ;; de um componente `:app`.
    [:app]))

;;
;; Associa um valor `value` a um chave `key` no
;; primeiro argumento da funcao `f`.
;; 
;; Espera-se que `f` seja um handler que possa
;; ser usado com o Ring.
;;
(defn wrap-assoc [f key value]
  (fn [request] (f (assoc request key value))))

(defrecord App [database handler]
  component/Lifecycle

  (start [component]
    (let [database (-> component :database :database)]
      (assoc component :handler
                       (wrap-assoc handler :database database)))))

(defn new-app [handler]
  (component/using
    (map->App {:handler handler})
    [:database]))

(defn list-users-handler [request]
  {:status 200
   :body (clojure.string/join "\n"
           (-> request
               :database ;; Aqui nós acessamos a chave `:database`
                         ;; que foi associada pelo componente `App`.
               (mc/find-maps :users)))})

;;
;; Apesar de podermos fazer isso diretamente no `-main`, aqui
;; podemos separar a responsabilidade de interpretar as
;; opcoes e passar para os respectivos constructores dos
;; componentes.
;;
(defn system [& options]
  (let [{:keys [database-url port]} options]
    (component/system-map
     :database (new-database database-url)
     :app      (new-app list-users-handler)
     :server   (new-web-server port))))

(defn -main
  [& args]
  (component/start (system :database-url (System/getenv "DATABASE_URL")
                           :port         3000)))
