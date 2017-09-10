<strong>Liste des Notes</strong> :
    <p>
    <table border="1">
        <tr align="center">
                <th>ID</th>
                <th>Titre</th>
        </tr>

<?php  


require_once 'Parsedown.php';
require_once 'dbconnect.php';

$query_titre = "SELECT * FROM note";
$result = $connect->query($query_titre);

$Parsedown = new Parsedown();

if($query_titre == true)
	{ 

	while ($donnees = $result->fetch_assoc()){

?>

	<tr>
		<td><a href="note.php?id=<?php echo $donnees['id']; ?>"><?php echo $donnees['id']; ?></a></td>
 		<td><?php echo $donnees['titre']; ?><br /></td>
	</tr>

<?php
		}

	}

$connect->close();
?>
    </table>
   </p>

