
 Notes etapes de construction d'un builder (humain)

 1. Est ce que toutes mes interfaces de builder sont présentes dans la classe ?
 public interface XXXPARAMBuilder
 content :
      Si mandatory => le contenu de l'interface :
          XXXNEXTPARAMBuilder PARAM(type PARAM)

      Si dernier param ou param optionel => le content :
          XXXBuilder PARAM(type PARAM)   V


 2. Est ce que le builder exist ? Non alors je le fait , Oui je regarde ce qu'il y a dedans
 Nommage XXXBuilder avec XXX nom de la classe private static final class
  => Implémente toutes les interfaces V

 3. Creation des champs du builder V

 4. Implementer les interfaces dans le builder 

 5. Si au moins un champ optionel => implémenter la methode build dans le builder

 6. Constructeur privé avec le builder

 7. public static XXXPREMIERPARAMBuilder builder() {
         return new XXXBuilder();
     }
