<?php
//Script de connexion à la BDD

$DB_HOST = "localhost";
$DB_USERNAME = "root";
$DB_PASSWORD = "root";
$DB_NAME = "writedown";

$connect = mysqli_connect($DB_HOST, $DB_USERNAME, $DB_PASSWORD, $DB_NAME);

if (!$connect)
	{
		die("Connection failed" . mysqli_connect_error());
	}

//mysqli_close($connect);

?>

