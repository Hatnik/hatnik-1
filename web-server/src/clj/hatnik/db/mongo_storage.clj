(ns hatnik.db.mongo-storage
  (:require [hatnik.db.storage :refer :all]
            [monger.core :as mg]
            [monger.collection :as mc]
            [monger.conversion :refer [to-object-id]]
            [clojure.set :refer [rename-keys]]))

(def users "users")
(def projects "projects")

(defn norm-id [map]
  (if map
    (-> map
        (rename-keys {:_id :id})
        (update-in [:id] str))
    map))

(deftype MongoStorage [db]


  hatnik.db.storage.UserStorage
  (get-user [storage email]
    (norm-id (mc/find-one-as-map db users {:email email})))

  (create-user! [storage email]
    (if-let [user (get-user storage email)]
      (:id user)
      (-> (mc/insert-and-return db users {:email email})
          norm-id
          :id)))


  hatnik.db.storage.ProjectStorage
  (get-projects [storage user-id]
    (->> (mc/find-maps db projects {:user-id user-id})
         (map norm-id)))

  (create-project! [storage data]
    (-> (mc/insert-and-return db projects data)
        norm-id
        :id))

  (update-project! [storage user-id id data]
    (mc/update db projects {:_id (to-object-id id)
                            :user-id user-id}
               data {:multi false}))

  (delete-project! [storage user-id id]
    (mc/remove db projects {:_id (to-object-id id)
                            :user-id user-id}))
)

(defn create-mongo-storage [{:keys [host port db drop?]}]
  (let [conn (mg/connect {:host host
                          :port port})]
    (when drop? (mg/drop-db conn db))
    (MongoStorage. (mg/get-db conn db))))

(comment

  (def st (create-mongo-storage {:host "localhost"
                                 :port 27017
                                 :db "test-me"}))

  (get-user st "hello")

  (create-user! st "hello")

  (get-projects st "hello")

  (create-project! st {:user-id "hello"
                       :name "hmm"})

  (update-project! st "hello" "5426b77944ae6f681ab5a422"
                   {:user-id "hello" :name "Hey"})

  )
