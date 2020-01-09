<?php


/**
 * Classe pour gérer toutes les opérations de db
 * Cette classe aura les méthodes CRUD pour les tables de base de données
 *
 */
class DbHandler {
    private $conn;
    function __construct() {
        require_once dirname(__FILE__) . '/DbConnect.php';
//Ouverture connexion db
        $db = new DbConnect();
        $this->conn = $db->connect();
    }

    /**Fonction qui retourne l'id d'une adresse dans la table adresse en indiquant le nom de la rue et la ville
     **/
    public function getId_Adresse($rue,$ville)
    {
        $adresse=$this->conn->prepare("SELECT id_adresse FROM adresse WHERE rue=? AND ville=?");
        $adresse->bind_param("ss",$rue,$ville);
        if ($adresse->execute()) {
            /** @var TYPE_NAME $adresse_id */
            $adresse->bind_result($adresse_id);
            $adresse->fetch();
            $adresse->close();
            return $adresse_id;
        } else {
            return NULL;
        }

    }

    /**
     * Fonction de creation d'une adresse
     * */
    public function createAdresse($rue,$ville){

        // requete d'insertion de l'adresse du client dans la table adresse
        $adresse=$this->conn->prepare("INSERT INTO adresse(rue,ville) VALUES (?,?)");
        $adresse->bind_param("ss",$rue,$ville);
       if($adresse->execute()){
           return ADRESSE_CREATED_SUCCESSFULLY;
       }
       else
       {
           return ADRESSE_CREATE_FAILED;
       }

    }
    /* ------------- méthodes de la table `client` ------------------ */
    /**
     * Creation nouvel utilisateur
     * @param String $nom nom complet de l'utilisateur
     * @param String $email email de connexion
     * @param String $password mot de passe de connexion
     * @return int
     */
    public function createClient($nom, $email, $telephone,$rue,$ville,$carte_bancaire,$password) {

        require_once 'PassHash.php'; //IMPORTATION DU FICHIER 'PassHash.php'

        // Vérifiez d'abord si il y'a utilisateur qui a le meme adresse ou le meme telephone
        if (!$this->isEmailExists($email) && !$this->isTelephoneExists($telephone)) {
//Générer un hash de mot de passe
            $password_hash = PassHash::hash($password);
// Générer API key
            $api_key = $this->generateApiKey();

// requete d'insertion de l'adresse du client dans la table adresse
               $this->createAdresse($rue,$ville);

               //si la creation de l'adresse s'est bien passer on recupere l'id de l'addresse pour l'inserer dans la table client

            $res_id=$this->getId_Adresse($rue,$ville);

                $stmt = $this->conn->prepare("INSERT INTO client(nom_client, email_client,telephone_client,id_adresse,carte_bancaire_client,
 password_hash,api_key) values(?,?, ?, ?, ?,?,?)");
                $stmt->bind_param("ssiiiss", $nom, $email,$telephone,$res_id,$carte_bancaire, $password_hash, $api_key);
                $result = $stmt->execute();



                //Vérifiez pour une insertion réussie
                if ($result) {
                // Utilisateur inséré avec succès
                    return USER_CREATED_SUCCESSFULLY;
                } else {
                 //Échec de la création de l'utilisateur
                    return USER_CREATE_FAILED;
                }


        } else {
//Utilisateur avec la même email existait déjà dans la db
            return USER_ALREADY_EXISTED;
        }
    }
    /**
     * Vérification de connexion de l'utilisateur
     * @param String $email
     * @param String $password
     * @return boolean Le statut de connexion utilisateur réussite / échec
     */
    public function checkLogin($email, $password) {
// Obtention de l'utilisateur par email
        $stmt = $this->conn->prepare("SELECT password_hash FROM client WHERE email_client = ?");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        /** @var TYPE_NAME $password_hash */
        $stmt->bind_result($password_hash);
        $stmt->store_result();
        if ($stmt->num_rows > 0) {
// Utilisateur trouvé avec l'e-mail
// Maintenant, vérifier le mot de passe
            $stmt->fetch();
            $stmt->close();
            if (PassHash::check_password($password_hash, $password)) {
// Mot de passe utilisateur est correcte
                return TRUE;
            } else {
// mot de passe utilisateur est incorrect
                return FALSE;
            }
        } else {

            $stmt->close();
// utilisateur n'existe pas avec l'e-mail
            return FALSE;
        }
    }


    /**
     * Vérification de connexion du Caissier
     * @param String $email
     * @param String $password
     * @return boolean Le statut de connexion utilisateur réussite / échec
     */
    public function checkLoginCaissier($email, $password) {
// Obtention de l'utilisateur par email
        $stmt = $this->conn->prepare("SELECT password_hash FROM caissier WHERE email_caissier = ?");

        $stmt->bind_param("s", $email);
        $stmt->execute();
        /** @var TYPE_NAME $password_hash */
        $stmt->bind_result($password_hash);
        $stmt->store_result();
        if ($stmt->num_rows > 0) {
// Caissier trouvé avec l'e-mail
// Maintenant, vérifier le mot de passe
            $stmt->fetch();
            $stmt->close();
            if ($password==$password_hash) {
// Mot de passe du caissier est correcte
                return TRUE;
            } else {
// mot de passe utilisateur est incorrect
                return FALSE;
            }
        } else {

            $stmt->close();
// le Caissier n'existe pas avec l'e-mail
            return FALSE;
        }
    }






    /**
     * Vérification de l'utilisateur en double par adresse e-mail
     * @param String $email email à vérifier dans la db
     * @return boolean
     */





    private function isEmailExists($email) {
        $stmt = $this->conn->prepare("SELECT id_client from client WHERE email_client = ?");
        $stmt->bind_param("s", $email);
        $stmt->execute();

        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }

    /**
     * Vérification de l'utilisateur en double par telephone
     * @param String $email email à vérifier dans la db
     * @return boolean
     */
    private function isTelephoneExists($telephone) {
        $stmt = $this->conn->prepare("SELECT id_client from client WHERE telephone_client = ?");
        $stmt->bind_param("i", $telephone);
        $stmt->execute();

        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }

    /**
     * Fonction qui retourne l'id d'un supermarcé dans  la table supermarche a partir d'un id donne
     * @param $nom_supermarche
     */
    public function getIdSupermarche($nom_sup){

        $stmt=$this->conn->prepare("SELECT id_supermarche FROM supermarche WHERE nom_supermarche= ?");

        $stmt->bind_param("s",$nom_sup);

        if ($stmt->execute()){

            $stmt->bind_result($id_sup);
            $stmt->fetch();
            $stmt->close();
            return $id_sup;
        }
        else{
            return null;
        }
    }


    /**
     * Obtention des Infos d'un caissier par son email
     */
    public function getInfoCaissier($email){
        $stmt=$this->conn->prepare("SELECT email_caissier,nom_caissier FROM caissier WHERE email_caissier = ?");

        $stmt->bind_param("s",$email);

       $stmt->execute();

           $stmt->bind_result($email_caissier,$nom_caissier);
           $stmt->fetch();
           $caissier=array();
           $caissier['email']=$email_caissier;
          // $caissier['nom_supermarche']=$nom_supermarche;
           $caissier['nom_caissier']=$nom_caissier;
           $stmt->close();
           return $caissier;


    }


    /**
     * Fonction qui retourne les infos d'un client en donnant son  id
     */

    public function getClientById($id_client){
        $stmt=$this->conn->prepare("SElECT nom_client,email_client,telephone_client,rang_client,url_photo_profil,api_key FROM client WHERE id_client= ?");
        $stmt->bind_param("i",$id_client);
        if ($stmt->execute()){

            $stmt->bind_result($nom_client,$email_client,$telephone_client,$rang_client,$url_photo_profil,$api_key);
            $stmt->fetch();
            $client=array();
            $client["nom_client"]=$nom_client;
            $client["email_client"]=$email_client;
            $client["telephone_client"]=$telephone_client;
            $client["rang_client"]=$rang_client;
            $client["url_photo_profil"]=$url_photo_profil;
            $client["api_key"]=$api_key;

            $stmt->close();

            return $client;


        }
        else{
            return null;
        }
    }


    /**
     *Obtention de l'utilisateur par email
     * @param String $email
     * @return array|null
     */
    public function getClientByEmail($email) {
        $stmt = $this->conn->prepare("SELECT client.nom_client,client.email_client,client.telephone_client,
 adresse.ville,adresse.rue,client.carte_bancaire_client,client.rang_client,client.url_photo_profil,client.api_key FROM client,adresse
  WHERE client.id_adresse=adresse.id_adresse AND client.email_client= ?");
        $stmt->bind_param("s", $email);
        if ($stmt->execute()) {
            $stmt->bind_result($nom,$email,$telephone,$ville,$rue,$carte_bancaire,$rang,$urlprofil,$api_key);
            $stmt->fetch();
            $client = array();
            $client["nom"] = $nom;
            $client["email"] = $email;
            $client["telephone"] = $telephone;
            $client["rue"] = $rue;
            $client["ville"] = $ville;
            $client["carte_bancaire"] = $carte_bancaire;
            $client["rang"] = $rang;
            $client["url_profil"] = $urlprofil;
            $client["api_key"] = $api_key;

            $stmt->close();
            return $client;
        } else {
            return NULL;
        }
    }


    /**
     * Obtention de la clé API de l'utilisateur
     * @param String $user_id clé primaire de l'utilisateur
     * @return null
     */
    public function getApiKeyById($user_id) {
        $stmt = $this->conn->prepare("SELECT api_key FROM users WHERE id = ?");
        $stmt->bind_param("i", $user_id);
        if ($stmt->execute()) {
            /** @var TYPE_NAME $api_key */
            $stmt->bind_result($api_key);
            $stmt->close();
            return $api_key;
        } else {
            return NULL;
        }
    }

    /**
     * Obtention de l'id d'un client a partir de son clé API
     * @param String $api_key
     * @return null
     */
    public function getClientId($api_key) {

        $stmt = $this->conn->prepare("SELECT id_client FROM client WHERE api_key = ?");
        $stmt->bind_param("s", $api_key);
        if ($stmt->execute()) {
            /** @var TYPE_NAME $user_id */
            $stmt->bind_result($user_id);
            $stmt->fetch();
            $stmt->close();
            return $user_id;
        } else {
            return NULL;
        }
    }
    /**
     * Validation de la clé API de l'utilisateur
     * Si la clé API est là dans db, elle est une clé valide
     * @param String $api_key
     * @return boolean
     */
    public function isValidApiKey($api_key) {
        $stmt = $this->conn->prepare("SELECT id_client from client WHERE api_key = ?");
        $stmt->bind_param("s", $api_key);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }
    /**
     * Génération aléatoire unique MD5 String pour utilisateur clé Api
     */
    private function generateApiKey() {
        return md5(uniqid(rand(), true));
    }
    /* ------------- méthodes table`tasks` ------------------ */
    /**
     * Creation nouvelle tache
     * @param String $user_id id de l'utilisateur à qui la tâche appartient
     * @param String $task texte de la tache
     * @return null
     */
    public function createTask($user_id, $task) {
        $stmt = $this->conn->prepare("INSERT INTO tasks(task) VALUES(?)");
        $stmt->bind_param("s", $task);
        $result = $stmt->execute();
        $stmt->close();
        if ($result) {
// ligne de tâche créé
// maintenant assigner la tâche à l'utilisateur
            $new_task_id = $this->conn->insert_id;
            $res = $this->createUserTask($user_id, $new_task_id);
            if ($res) {
// tâche créée avec succès
                return $new_task_id;
            } else {
//tâche n'a pas pu être créé
                return NULL;
            }
        } else {
//tâche n'a pas pu être créé
            return NULL;
        }
    }

    /**
     * Obtention d'une seule tâche
     * @param String $task_id id de la tâche
     * @param $user_id
     * @return array|null
     */

    public function getTask($task_id, $user_id) {
        $stmt = $this->conn->prepare("SELECT t.id, t.task, t.status, t.created_at from 
tasks t, user_tasks ut WHERE t.id = ? AND ut.task_id = t.id AND ut.id_client = ?");
        $stmt->bind_param("ii", $task_id, $user_id);
        if ($stmt->execute()) {
            $res = array();
            $stmt->bind_result($id, $task, $status, $created_at);
            $stmt->fetch();
            $res["id"] = $id;
            $res["task"] = $task;
            $res["status"] = $status;
            $res["created_at"] = $created_at;
            $stmt->close();
            return $res;
        } else {
            return NULL;
        }
    }

    /**
     *Obtention de tous les tâches de l'utilisateur
     * @param String $user_id id de l'utilisateur
     * @return
     */
    public function getAllUserTasks($user_id) {
        $stmt = $this->conn->prepare("SELECT t.* FROM tasks t, user_tasks ut WHERE t.id =
ut.task_id AND ut.id_client = ?");
        $stmt->bind_param("i", $user_id);
        $stmt->execute();
        $tasks = $stmt->get_result();
        $stmt->close();
        return $tasks;
    }

    /**
     * Mise à jour de la tâche
     * @param $user_id
     * @param String $task_id id de la tâche
     * @param String $task Le texte de la tâche
     * @param String $status le statut de la tâche
     * @return bool
     */
    public function updateTask($user_id, $task_id, $task, $status) {
        $stmt = $this->conn->prepare("UPDATE tasks t, user_tasks ut set t.task = ?,
t.status = ? WHERE t.id = ? AND t.id = ut.task_id AND ut.id_client = ?");
        $stmt->bind_param("siii", $task, $status, $task_id, $user_id);
        $stmt->execute();
        $num_affected_rows = $stmt->affected_rows;
        $stmt->close();
        return $num_affected_rows > 0;
    }

    /**
     *
     * Function de mis a jours d'un utilisateur
     * @param $user_id
     * @param $task_id
     * @param $task
     * @param $status
     * @return bool
     *
     */
    public function updateUser($user_id, $task_id, $task, $status) {

        $stmt = $this->conn->prepare("UPDATE tasks t, user_tasks ut set t.task = ?,
t.status = ? WHERE t.id = ? AND t.id = ut.task_id AND ut.id_client = ?");
        $stmt->bind_param("siii", $task, $status, $task_id, $user_id);
        $stmt->execute();
        $num_affected_rows = $stmt->affected_rows;
        $stmt->close();
        return $num_affected_rows > 0;
    }

    /**
     * Suppression d'une tâche
     * @param $user_id
     * @param String $task_id id de la tâche à supprimer
     * @return bool
     */
    public function deleteTask($user_id, $task_id) {
        $stmt = $this->conn->prepare("DELETE t FROM tasks t, user_tasks ut WHERE t.id = ?
AND ut.task_id = t.id AND ut.id_client = ?");
        $stmt->bind_param("ii", $task_id, $user_id);
        $stmt->execute();
        $num_affected_rows = $stmt->affected_rows;
        $stmt->close();
        return $num_affected_rows > 0;
    }
/* ------------- méthode de la table`user_tasks` ------------------ */
    /**
     * Fonction d'assigner une tâche à l'utilisateur
     * @param String $user_id id de l'utilisateur
     * @param String $task_id id de la tâche
     * @return
     */
    public function createUserTask($user_id, $task_id) {
        $stmt = $this->conn->prepare("INSERT INTO user_tasks(id_client, task_id) values(?,
?)");
        $stmt->bind_param("ii", $user_id, $task_id);
        $result = $stmt->execute();
        if (false === $result) {
            die('execute() failed: ' . htmlspecialchars($stmt->error));
        }
        $stmt->close();
        return $result;
    }


    /**
     * Fonction qui va retourne la caisse possedant le moins de personne dans un supermrché donné
     * @param $supermarche
     */

    public function caisseMini($id_supermarche){

        $caisse=array();

        /**
         * requete qui renvoie le premier resultat trouver,donc si il existent +sieurs caisse qui possede le meme nombre de client
         * que le Minimum trouver alors on renvoyera le 1ere resultat trouver
         */
        $stmt=$this->conn->prepare("SELECT num_caisse,nb_client FROM caisse WHERE id_supermarche= ?
AND nb_client = (SELECT MIN(nb_client) FROM caisse GROUP BY id_supermarche HAVING id_supermarche= ?) LIMIT 1");

        $stmt->bind_param("ii",$id_supermarche,$id_supermarche);
        if($stmt->execute()){

            $stmt->bind_result($num_caisse,$nb_client);
            $stmt->fetch();
            $stmt->close();
            $caisse["nb_client"]=$nb_client;
            $caisse["num_caisse"]=$num_caisse;


            return $caisse;
        }
        else{
            return null;
        }


    }

    /**
     *
     * Fonction qui retourne le maximum des numero de ticket dans un caisse et une supermarché donné
     * @param $id_caisse
     * @param $id_supermarche
     * @return null
     */
    public function maxNumCaisseFil($id_caisse){

        //$stmt=$this->conn->prepare("SELECT (num_ticket) FROM ticket GROUP BY id_caisse HAVING id_caisse = ?");
        $stmt=$this->conn->prepare("SELECT num_ticket FROM ticket WHERE num_ticket=(SELECT MAX(num_ticket) FROM ticket GROUP BY id_caisse
 HAVING id_caisse= ? ) AND id_caisse= ?");

        $stmt->bind_param("ii",$id_caisse,$id_caisse);
        if ($stmt->execute()) {



            $stmt->bind_result($num_max);
            $stmt->fetch();
            $stmt->close();
            if ($num_max!=null){

                return $num_max;

            }
            else{
                return null;
            }


        }

    }


    /**
     * Fonction qui supprime un ticket donne par dans la fil d'attente
     * @param $id_client
     * @return mixed
     */
    public function supprimeTicketFil($num_ticket,$id_caisse){


        $stmt=$this->conn->prepare("DELETE FROM ticket WHERE  num_ticket = ? AND id_caisse = ?");
        $stmt->bind_param("ii",$num_ticket,$id_caisse);


        return $stmt->execute();

    }









    /**
     * Fonction qui retourne le nombre de client qui se trouve dans une fil d'attente d'une supermarché
     * c.a.d la somme des client qui sont en attente dans tous ses caisses
     * @param $id_supermarche
     * @return null
     */
     public  function  nb_clientFil(){

        $stmt=$this->conn->prepare("SELECT SUM(nb_client) FROM caisse WHERE id_supermarche= 1");
        if($stmt->execute()){

            $stmt->bind_result($nb_client);
            $stmt->fetch();
            $stmt->close();
            return $nb_client;
        }
        else{
            return null;
        }

     }


    /**
     * Fonction qui ajoute un client dans une fil d'attente
     *
     *
     * @param $url_qrCode
     * @param $num_ticket
     * @param $id_client
     * @param $id_caisse
     * @param $id_supermarche
     * @param $temps
     * @return mixed
     */


     public function ajoutClientFil($url_qrCode,$num_ticket,$id_client,$id_caisse,$temps){
         $stmt=$this->conn->prepare("INSERT INTO ticket ( url_qrcode, num_ticket, id_client, id_caisse, temps_attente) VALUES 
(? ,? ,? ,? ,? )");

         $stmt->bind_param("siiii",$url_qrCode,$num_ticket,$id_client,$id_caisse,$temps);

        return $stmt->execute();
     }



    /**
     * Fonction qui enleve de la fil d'attente un client donne
     * @param $id_client
     * @return bool
     */

    public function enleve_ClientFil($id_client){
        $stmt=$this->conn->prepare("DELETE FROM ticket WHERE id_client=? ");
        $stmt->bind_param("i",$id_client);
        if ($stmt->execute()){
            return true;
        }
        else
            return false;
    }





}









