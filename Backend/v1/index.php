<?php

require_once '../include/DbHandler.php';
require_once '../include/PassHash.php';
require '.././libs/Slim/Slim.php';
\Slim\Slim::registerAutoloader();

$app = new \Slim\Slim();

require_once '../vendor/autoload.php';


// ID utilisateur - variable globale
$id_client = NULL;

/**
 * Vérification params nécessaires posté ou non
 */
function verifyRequiredParams($required_fields) {
    $error = false;
    $error_fields = "";
    $request_params = array();
    $request_params = $_REQUEST;
// Manipulation paramsde la demande PUT
    if ($_SERVER['REQUEST_METHOD'] == 'PUT') {
        $app = \Slim\Slim::getInstance();
        parse_str($app->request()->getBody(), $request_params);
    }
    foreach ($required_fields as $field) {
        if (!isset($request_params[$field]) || strlen(trim($request_params[$field])) <= 0)
        {
            $error = true;
            $error_fields .= $field . ', ';
        }
    }
    if ($error) {
//Champ (s) requis sont manquants ou vides
// echo erreur JSON et d'arrêter l'application

        $response = array();
        $app = \Slim\Slim::getInstance();
        $response["error"] = true;
        $response["message"] = 'Champ(s) requis ' . substr($error_fields, 0, -2) . ' est
(sont) manquant(s) ou vide(s)';
        echoRespnse(400, $response);
        $app->stop();
    }
}
/**
 * Validation adresse e-mail
 */
function validateEmail($email) {
    $app = \Slim\Slim::getInstance();
    if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $response["error"] = true;
        $response["message"] = "Adresse e-mail n'est pas valide";
        echoRespnse(400, $response);
        $app->stop();
    }
}
/**
 * Faisant écho à la réponse JSON au client
 * @param String $status_code Code de réponse HTTP
 * @param Int $response response Json
 */
function echoRespnse($status_code, $response) {
    $app = \Slim\Slim::getInstance();
// Code de réponse HTTP
    $app->status($status_code);
// la mise en réponse type de contenu en JSON
    $app->contentType('application/json');
    echo utf8_encode(json_encode($response));
}

$app->post('/register_adresse',function () use ($app)
{
    $db=new DbHandler();
    $rue=$app->request()->post('rue');
    $ville=$app->request()->post('ville');
    $res=$db->createAdresse($rue,$ville);


    if ($res == ADRESSE_CREATED_SUCCESSFULLY) {
        $response["error"] = false;
        $response["message"] = "Vous êtes inscrit avec succès";
    } else if ($res == ADRESSE_CREATE_FAILED) {
        $response["error"] = true;
        $response["message"] = "Oops! Une erreur est survenue lors de la creation de l'addresse";
    } else {
        $response["error"] = true;
        $response["message"] = "Désolé, cet E-mail éxiste déja";
    }
// echo de la repense JSON
    echoRespnse(201, $response);

});
/**
 * Enregistrement de l'utilisateur
 * url - /register
 * methode - POST
 * params - name, email, password
 */
$app->post('/register', function() use ($app) {
// vérifier les paramètres requises
    verifyRequiredParams(array('nom_client','email_client', 'telephone_client','rue','ville','carte_bancaire_client', 'password'));
    $response = array();
// lecture des params de post
    $nom = $app->request->post('nom_client');
    $email = $app->request->post('email_client');
    $telephone = $app->request->post('telephone_client');
    $rue = $app->request->post('rue');
    $ville=$app->request->post('ville');
    $carte = $app->request->post('carte_bancaire_client');
    $password = $app->request->post('password');
// valider adresse email
    validateEmail($email);
    $db = new DbHandler();
    $res = $db->createClient($nom, $email,$telephone,$rue,$ville,$carte, $password);
    if ($res == USER_CREATED_SUCCESSFULLY) {
        $response["error"] = false;
        $response["message"] = "Vous êtes inscrit avec succès";
    } else if ($res == USER_CREATE_FAILED) {
        $response["error"] = true;
        $response["message"] = "Oops! Une erreur est survenue lors de l'inscription";
    } else if ($res == USER_ALREADY_EXISTED) {
        $response["error"] = true;
        $response["message"] = "Désolé, cet E-mail éxiste déja";
    }
// echo de la repense JSON
    echoRespnse(201, $response);
});



/**
 * Login Utilisateur
 * url - /login
 * method - POST
 * params - email, password
 */
$app->post('/login', function() use ($app) {
// vérifier les paramètres requises
    verifyRequiredParams(array('email', 'password'));
// lecture des params de post
    $email = $app->request()->post('email');
// valider l'adresse email
    validateEmail($email);
    $password = $app->request()->post('password');
    $response = array();
    $db = new DbHandler();

    //On verifie d'abord si l'email et le mot de passe donne corresponds se trouve dans la table caissier
    if($db->checkLoginCaissier($email,$password)){

        //on recupere les infos du cassier
        $caissier=$db->getInfoCaissier($email);

        //Si la reponse est different de Null on met les infos renvoyé dans la variable reponse du requete
        if($caissier!=NULL)
        {
            $response['error']=false;
            $response['status']='caissier';
            $response['nom']=$caissier['nom_caissier'];
            $response['email']=$caissier['email'];
            $response['telephone'] =null;
            $response['rue'] =null;
            $response['ville'] = null;
            $response['carte_bancaire'] = null;
            $response['rang'] =null;
            $response['carte_bancaire'] = null;
            $response['url_profil'] = null;
            $response['apiKey'] = null;


        }
        else {
// erreur inconnue est survenue
            $response['error'] = true;
            $response['message'] = "Une erreur est survenue. S'il vous plaît essayer à nouveau";
        }

    }
    elseif ($db->checkLogin($email, $password)){  //Sinon on veriie si l'email et le mdp donne corresponds a un utilisateur

        $client = $db->getClientByEmail($email);//On recupere les infos de l'utilisateur
        if ($client != NULL) {

                $response['error'] = false;
                $response['status']="client";
                $response['nom'] = $client['nom'];
                $response['email'] = $client['email'];
                $response['telephone'] = $client['telephone'];
                $response['rue'] = $client['rue'];
                $response['ville'] = $client['ville'];
                $response['carte_bancaire'] = $client['carte_bancaire'];
                $response['rang'] = $client['rang'];
                $response['carte_bancaire'] = $client['carte_bancaire'];
                $response['url_profil'] = $client['url_profil'];
                $response['apiKey'] = $client['api_key'];

        } else {
// erreur inconnue est survenue
            $response['error'] = true;
            $response['message'] = "Une erreur est survenue. S'il vous plaît essayer à nouveau";
        }
    } else {
// identificateurs de l'utilisateur sont erronés
        $response['error'] = true;
        $response['message'] = 'Échec de la connexion. identificateurs incorrectes';}

    echoRespnse(200, $response);
});



/**
 * Ajout de Couche intermédiaire pour authentifier chaque demande
 * Vérifier si la demande a clé API valide dans l'en-tête "Authorization"
 *
 * La méthode authenticate () sera exécutée à chaque fois avant de faire des
 * opérations liées à la tâche sur la base de données
 */
function authenticate(\Slim\Route $route) {

// Obtenir les en-têtes de requêtes
    $headers = apache_request_headers();
    $response = array();
    $app = \Slim\Slim::getInstance();
// Vérification de l'en-tête d'autorisation
    if (isset($headers['Authorization'])) {
        $db = new DbHandler();
// Obtenir la clé d'api
        $api_key = $headers['Authorization'];
// Valider la clé API
        if (!$db->isValidApiKey($api_key)) {
// Clé API n'est pas présente dans la table des utilisateurs
            $response["error"] = true;
            $response["message"] = "Accès Refusé. Clé API invalide";
            echoRespnse(401, $response);
            $app->stop();
        } else {
            global $id_client;
// Obtenir l'ID utilisateur (clé primaire)
            $id_client = $db->getClientId($api_key);
        }
    } else {
// Clé API est absente dans la en-tête
        $response["error"] = true;
        $response["message"] = "Clé API est manquante";
        echoRespnse(400, $response);
        $app->stop();
    }
}



/**
 *Création d'une nouvelle tâche dans db
 * method POST
 * params - name
 * url - /tasks/
 */
$app->post('/tasks', 'authenticate', function() use ($app) {
// vérifier les paramètres requises
    verifyRequiredParams(array('task'));
    $response = array();
    $task = $app->request->post('task');
    global $id_client;
    $db = new DbHandler();
//Création d'une nouvelle tâche
    $task_id = $db->createTask($id_client, $task);
    if ($task_id != NULL) {
        $response["error"] = false;
        $response["message"] = "Tâche créé avec succès";
        $response["task_id"] = $task_id;
        echoRespnse(201, $response);
    } else {$response["error"] = true;
        $response["message"] = "Impossible de créer la tâche. S'il vous plaît
essayer à nouveau";
        echoRespnse(200, $response);
    }
});



/**
 *Lister toutes les tâches d'un utilisateur particulier
 * method GET
 * url /tasks
 */

/**$app->get('/Caissier/:email_caissier', function($email_caissier) {
   global $id_client;
    $response = array();
    $db = new DbHandler();


// aller chercher toutes les infos d'un caissier dont on a indiquer l'email
    $result = $db->getInfoCaissier($email_caissier);
    $response["error"] = false;
    $response["tasks"] = array();
// boucle au travers du résultat et de la préparation du tableau des tâches
    while ($task = $result->fetch_assoc()) {
        $tmp = array();
        $tmp["id"] = $task["id"];
        $tmp["task"] = $task["task"];
        $tmp["status"] = $task["status"];
        $tmp["createdAt"] = $task["created_at"];
        array_push($response["tasks"], $tmp);
    }echoRespnse(200, $response);
});*

 * */

/**
 *Lister une seule tâche d'un utilisateur particulier
 * method GET
 * url /tasks/:id
 *Retournera 404 si la tâche n'appartient pas à l'utilisateur
 */
$app->get('/caissier/:email_caissier', function($email_caissier) {

    $response = array();
    $db = new DbHandler();
//chercher tâche
    $result = $db->getInfoCaissier($email_caissier);
    if ($result != NULL) {

        $response["error"] = false;
        $response["nom_caissier"] = $result["nom_caissier"];
        $response["email_caissier"] = $result["email_caissier"];
        $response["nom_supermarche"] = $result["nom_supermarche"];

        echoRespnse(200, $response);

    } else {
        $response["error"] = true;
        $response["message"] = "La ressource demandée n'existe pas";
        echoRespnse(404, $response);
    }
});

/**
 * fontion qui retourne un ticket a un utilisateur,l'utilisateur doit son clé API dans l'en-tete de la requete pour pouvoir demander un ticket
 *
 */
$app->get('/ticket/:supermarche','authenticate',function ($supermarche) use($app){

    $reponse=array();
    $db=new DbHandler();

    global $id_client; //on fait appelle a la variable id_client qui contient l'id du client



    $id_sup=$db->getIdSupermarche($supermarche);//recupere l'id du supermarche dans la table supermarché
    if ($id_sup!=null){



        /**recherche la caisse qui possede le moins de personne dans le supermarché
         * Ce qui retourne un tableau associatif contenant le numero de la caisse et le nombre de personne present dans la caisse
         * */
        $caisse_mini=$db->caisseMini($id_sup);

        if ($caisse_mini!=null){

            /**
             * recherche du plus haut numero actuelle de cette caisse dans la fil d'attente du supermarché donne
             */
            $num_max=$db->maxNumCaisseFil($caisse_mini);

            if ($num_max!=null){

                $caisse=$caisse_mini["num_caisse"];//on recupere le numero de la caisse minimal et on le stocke dans la variable $caisse

                $num_ticket=$num_max+1;//on incremente le numero maximal actuelle de la caisse qui possede le moins de personne
                // et on le resultat dans la variable num_ticket



                /** Generation du QrCode du Ticket**/

                $qrCode=new Endroid\QrCode\QrCode($num_max+1);//Ecriture du Numero de Ticket generer dans le QrCode

                $qrCode->setSize(300);//taille du QrCode
                $qrCode->setWriterByName('png');
                $qrCode->setMargin(10);
                $qrCode->setEncoding('UTF-8');
                $qrCode->setErrorCorrectionLevel(\Endroid\QrCode\ErrorCorrectionLevel::HIGH);
                $qrCode->setForegroundColor(['r' => 0, 'g' => 0, 'b' => 0, 'a' => 0]);
                $qrCode->setBackgroundColor(['r' => 255, 'g' => 255, 'b' => 255, 'a' => 0]);





                $urlBd='../images/qrcodes/ticket'.$num_ticket.'_caisse'.$caisse.'.png';



                /**
                 * Preparation de la reponse du requete d'un demande de Ticket
                 */
                $reponse["error"]=false;
                $reponse["num_ticket"] = $num_max + 1; //le num de ticket renvoyé sera le dernier numero dans la caisse + 1
                $reponse["num_caisse"] = $caisse_mini['num_caisse'];
                $reponse["url_qrcode"]='images/qrcodes/ticket'.$num_ticket.'_caisse'.$caisse.'.png';
                $reponse["id_supermarche"]=$id_sup;
                $reponse["temps_attente"]=1;

                /**Insertion du client qui a demande le ticket dans la fil d'attente,on verifie si le client existe dans la fil d'attente d'abord*/

              if ( $db->ajoutClientFil($urlBd,$num_max+1,$id_client,$caisse_mini["num_caisse"],20)){


                  //sauvegarde de l'image du QrCode dans le dossier des QrCode
                  $qrCode->writeFile('../images/qrcodes/ticket'.$num_ticket.'_caisse'.$caisse.'.png');

                  echoRespnse(200,$reponse);//envoie de la reponse du requete
              }
              else{

                  $reponse2["error"]=true;
                  $reponse2["message"]="Client existe deja dans la fil d'attente";
                  echoRespnse(201,$reponse2);
              }



            }


        }
        else{
            $reponse["error"]=true;
            $reponse["message"]="Aucun caisse minimal trouver";

            echoRespnse(401,$reponse);
        }

    }
    else{

        $response["error"] = true;
        $response["message"] = "Aucun ticket trouver ";
        echoRespnse(404,$reponse);

    }
});

/**
 * Requete pour avoir la caisse qui possede le plus de personne dans la fil
 */
$app->get("/max/:id_caisse",function ($id_caisse){

    //verifrifier les paramètres requises
   // verifyRequiredParams(array('task'));
    $response = array();



    $db = new DbHandler();
//Création d'une nouvelle tâche
    $max = $db->maxNumCaisseFil($id_caisse);
    if ($max!=null) {
        $response["error"] = false;
        $response["message"] = "Caisse mis a jour avec succès";
        $response["num_max"] = $max;
        echoRespnse(201, $response);
    } else {$response["error"] = true;

        $response["num_max"] = $max;
        $response["message"] = "Impossible de mettre a jour la caisse. S'il vous plaît
essayer à nouveau";
        echoRespnse(200, $response);
    }



});

/**
 * Mise à jour d'une tâche existante
 * method PUT
 * params task, status
 * url - /tasks/:id
 */
$app->put('/tasks/:id', 'authenticate', function($task_id) use($app) {
// vérifier les paramètres requises
    verifyRequiredParams(array('task', 'status'));
    global $id_client;
    $task = $app->request->put('task');
    $status = $app->request->put('status');
    $db = new DbHandler();
    $response = array();
// Mise à jour de la tâche
    $result = $db->updateTask($id_client, $task_id, $task, $status);
    if ($result) {
// Tache mise à jour
        $response["error"] = false;
        $response["message"] = "Tâche mise à jour avec succès";
    } else {
// Le mise à jour de la tâche a échoué.
        $response["error"] = true;
        $response["message"] = "Le mise à jour de la tâche a échoué. S'il vous
plaît essayer de nouveau!";
    }
    echoRespnse(200, $response);
});



/**
 * Suppression tâche. Les utilisateurs peuvent supprimer uniquement leurs tâches
 * method DELETE
 * url /tasks
 */
$app->delete('/tasks/:id', 'authenticate', function($task_id) use($app) {
    global $id_client;
    $db = new DbHandler();
    $response = array();
    $result = $db->deleteTask($id_client, $task_id);
    if ($result) {
// tâche supprimé avec succès
        $response["error"] = false;
        $response["message"] = "tâche supprimé avec succès";
    } else {
// "échec de la suppression d'une tâche.
        $response["error"] = true;
        $response["message"] = "échec de la suppression d'une tâche. S'il vous plaît essayer de nouveau!";
    }
    echoRespnse(200, $response);
});

/**
 * requete pour la suppression d'un client dans la fil d'attente
 */

$app->delete('/delete_ticket/:num_caisse/:num_ticket', function($num_caisse,$num_ticket) use($app) {

    $db = new DbHandler();
    $response = array();
    $result =$db->supprimeTicketFil($num_ticket,$num_caisse);//suppression du ticket
    if ($result) {
      //Numero de ticket supprimé avec succès
        $response["error"] = false;
        $response["message"] = "Numero de Ticket supprimé avec succès";

        /**On supprime aussi l'image de son QrCode dans le dossier des qrCode*/


        $repertoire = opendir ("../images/qrcodes/"); // On ouvre le dossier contenant les images des QrCode
                                                    // (dans une variable qui sera utilisée par la suite

        unlink("../images/qrcodes/ticket".$num_ticket."_caisse".$num_caisse.".png"); // On efface l'mage du QRCode


        closedir($repertoire); //fermeture du du repertoire

    } else {
        // "échec de la suppression du numero de ticket.
        $response["error"] = true;
        $response["message"] = "échec de la suppression du numero de ticket dans la fil. S'il vous plaît essayer de nouveau!";
    }
    echoRespnse(200, $response);
});


/**
 * Requete pour avoir le nombre de personne present dans le fil d'attente d'une supermarché donné
 */
$app->get("/nb_client_fil",function (){


    $db = new DbHandler();
//Création d'une nouvelle tâche
    $nb_client = $db->nb_clientFil();
    if ($nb_client!=null) {

        $response["error"] = false;
        $response["nb_client"] =(integer)$nb_client;

        echoRespnse(201, $response);
    } else {
        $response["error"] = true;
        $response["nb_client"] = null;
        echoRespnse(200, $response);
    }


});





$app->run();
