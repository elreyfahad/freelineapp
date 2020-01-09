<?php
/**
 * Ce fichier contient toute la configuration du projet
 * comme les paramètres de connexion de base de données et d’autres variables.
 */
define('DB_USERNAME', 'root');//defintion de l'utilisateur de la base de donné mysql de Wamp
define('DB_PASSWORD', '');//le mdp de l'utilisateur (ici c'est une chaine vide)
define('DB_HOST', 'localhost');//le host
define('DB_NAME', 'db_pfe');//le nom du BD

define('USER_CREATED_SUCCESSFULLY', 0);//CODE RENVOYE SI UN UTILISATEUR EST BIEN CREER
define('USER_CREATE_FAILED', 1);//CODE RENVOYE SI LA CREATION D'UN UTILISATEUR A ECHOUER
define('USER_ALREADY_EXISTED', 2);//CODE RENVOYER SI UN UTILISATUER EXISTE DEJA DANS LA BASE MAIS VEUT S'INSCRIRE

define('ADRESSE_CREATED_SUCCESSFULLY',0);
define('ADRESSE_CREATE_FAILED',1);