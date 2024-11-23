/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proy_2_arb_gen;

/**
 * Clase que representa un Lord de Game of Thrones
 */

public class Lord {
    /**
     * Algunas constantes necesarias para poder hacer parse del json con la
     * información
     * y para poder desplegar la información en un string de texto.
     */
    public static final String LORD_TITLE = "Lord";
    public static final String HOUSE_TITLE = "House";
    public static final String NUMERAL_TITLE = "Of his name";
    public static final String UNKNOWN_TITLE = "[Unknown]";
    public static final String FATHER_TITLE = "Born to";
    public static final String MOTHER_TITLE = "Born to";
    public static final String ALIAS_TITLE = "Known throughout as";
    public static final String TITLE_TITLE = "Held title";
    public static final String WED_TO_TITLE = "Wed to";
    public static final String EYES_COLOR_TITLE = "Of eyes";
    public static final String HAIR_COLOR_TITLE = "Of hair";
    public static final String CHILDREN_TITLE = "Father to";
    public static final String NOTES_TITLE = "Notes";
    public static final String FATE_TITLE = "Fate";

    /**
     * Algunos comparadores necesarios para encontrar los lords en arboles.
     */

    /**
     * Comparador por nombre
     */
    public static IComparador<Lord> comparadorNombre = (Lord a, Lord b) -> a.name.toLowerCase()
            .compareTo(b.name.toLowerCase());

    /**
     * Comparador por nombre completo
     */
    public static IComparador<Lord> comparadorNombreFull = (Lord a, Lord b) -> a.fullName.toLowerCase()
            .compareTo(b.fullName.toLowerCase());

    /**
     * Comparador por único nombre
     */
    public static IComparador<Lord> comparadorUnicoNombre = (Lord a, Lord b) -> a.uniqueName.toLowerCase()
            .compareTo(b.uniqueName.toLowerCase());

    /**
     * Comparador por alias
     */
    public static IComparador<Lord> comparadorAlias = (Lord a, Lord b) -> {
        if (a.alias == null && b.alias == null) {
            return -1;
        }
        String alias1 = (a.alias == null) ? "" : a.alias.toLowerCase();
        String alias2 = (b.alias == null) ? "" : b.alias.toLowerCase();
        return alias1.compareTo(alias2);
    };

    /**
     * Comparador por títulos
     */
    public static IComparador<Lord> comparadorTitle = (Lord a, Lord b) -> {
        if (a.title == null && b.title == null) {
            return -1;
        }
        String title1 = (a.title == null) ? "" : a.title.toLowerCase();
        String title2 = (b.title == null) ? "" : b.title.toLowerCase();
        return title1.compareTo(title2);
    };

    /**
     * La información del lord
     */
    public String fullName = null;
    public String name = null;
    public String lastName = null;
    public String uniqueName = null;
    public String house = null;
    public String numeral = null;
    public String father = null;
    public String mother = null;
    public String alias = null; // Mote
    public String title = null;
    public String wedTo = null;
    public String eyesColor = null;
    public String hairColor = null;
    public String[] children = null;
    public String notes = null;
    public String fate = null;
    /**
     * Indica si el lord es valido, es decir si ya tiene
     * cargada su información.
     */
    public boolean isValid = false;

    /**
     * Parser de un objeto json con la información del lord,
     * carga toda la información en los atributos que la representan
     * retorna true si el json es correcto o false en caso contrario.
     * 
     * @param json el objeto json con la información del lord
     * 
     * @return true si el json es correcto o false en caso contrario
     */
    public boolean parse(String json) {
        if (json == null) {
            return false;
        }
        if (json == "") {
            return false;
        }
        json = json.strip();
        if (!json.startsWith("{")) {
            return false;
        }
        if (!json.endsWith("}")) {
            return false;
        }
        // Quitar los espacios
        json = json.strip();
        // Quitamos las llaves al comienzo y al final
        if (json.startsWith("{")) {
            json = json.substring(1, json.length());
        }
        if (json.endsWith("}")) {
            json = json.substring(0, json.length() - 1);
        }
        // Quitar los espacios
        json = json.strip();
        // Quitar los espacios iniciales y finales
        json = json.strip();
        // Quitar las comillas
        json = json.replace("\"", "");
        // Quitar las llaves inicial y final
        if (json.startsWith("{")) {
            json = json.substring(1, json.length());
        }
        if (json.endsWith("}")) {
            json = json.substring(0, json.length() - 1);
        }
        // volvemos a quitar los espacios iniciales y finales
        json = json.strip();
        // Separar por dos puntos, para obtener el nombre completo y el resto de la
        // información
        String[] data = json.split(":", 2);
        fullName = data[0].strip();
        name = fullName.split(" ", 2)[0];
        lastName = fullName.split(" ", 2)[1];
        house = lastName;

        // Nos quedamos con el resto de la información
        json = data[1];
        // Volvemos a quitar los espacios iniciales y finales
        json = json.strip();
        if (json.startsWith("[")) {
            json = json.substring(1, json.length());
        } else {
            return false;
        }
        if (json.endsWith("]")) {
            json = json.substring(0, json.length() - 1);
        } else {
            return false;
        }
        // volvemos a quitar los espacios iniciales y finales
        json = json.strip();

        // Quitamos los \n y los \r ,
        json = json.replace("\n", "");
        json = json.replace("\r", "");

        // Las comas que están fuera de [] y fuera de {} , se reemplazan por \n
        int numCorchetes = 0;
        int numLlaves = 0;
        String jsonTxt = "";
        for (int i = 0; i < json.length(); i++) {
            if (json.charAt(i) == '[') {
                numCorchetes++;
            }
            if (json.charAt(i) == ']') {
                numCorchetes--;
            }
            if (json.charAt(i) == '{') {
                numLlaves++;
            }
            if (json.charAt(i) == '}') {
                numLlaves--;
            }
            if (json.charAt(i) == ',' && numCorchetes == 0 && numLlaves == 0) {
                jsonTxt += "\n";
                continue;
            }
            jsonTxt += json.charAt(i);
        }
        // ahora, cada linea va a ser un campo que quiero recuperar
        String[] fields = jsonTxt.split("\n");

        // ahora vamos a ir campo por campo viendo como obtenemos los datos
        for (String field : fields) {
            // Quitar los espacios iniciales y finales
            field = field.strip();
            // Quitar las llaves inicial y final
            if (field.startsWith("{")) {
                field = field.substring(1, field.length());
            } else {
                return false;
            }
            if (field.endsWith("}")) {
                field = field.substring(0, field.length() - 1);
            } else {
                return false;
            }

            // obtenemos el nombre del campo y el valor del campo
            String[] fieldData = field.split(":", 2);
            // le quitamos espacios iniciales y finales
            String fieldTitle = fieldData[0].strip();
            String fieldValue = fieldData[1].strip();

            // ahora, vamos a ir viendo los campos y sus valores
            if (fieldTitle.equals(NUMERAL_TITLE)) {
                // si tenemos un numeral, tenemos un uniqueName
                this.numeral = fieldValue;
                this.uniqueName = this.fullName + ", " + this.numeral + " " + Lord.NUMERAL_TITLE.toLowerCase();
                continue;
            }
            if (fieldTitle.equals(FATHER_TITLE)) {
                if (fieldValue.equals(UNKNOWN_TITLE)) {
                    continue;
                }
                if (this.father == null) {
                    this.father = fieldValue;
                } else {
                    this.mother = fieldValue;
                }
                continue;
            }
            if (fieldTitle.equals(ALIAS_TITLE)) {
                this.alias = fieldValue;
                continue;
            }
            if (fieldTitle.equals(TITLE_TITLE)) {
                this.title = fieldValue;
                continue;
            }
            if (fieldTitle.equals(WED_TO_TITLE)) {
                this.wedTo = fieldValue;
                continue;
            }
            if (fieldTitle.equals(EYES_COLOR_TITLE)) {
                this.eyesColor = fieldValue;
                continue;
            }
            if (fieldTitle.equals(HAIR_COLOR_TITLE)) {
                this.hairColor = fieldValue;
                continue;
            }
            if (fieldTitle.equals(CHILDREN_TITLE)) {
                // el campo children es un array, por lo que necesitamos crear un arreglo con
                // los valores
                if (fieldValue.startsWith("[")) {
                    fieldValue = fieldValue.substring(1, fieldValue.length());
                }
                if (fieldValue.endsWith("]")) {
                    fieldValue = fieldValue.substring(0, fieldValue.length() - 1);
                }
                fieldValue = fieldValue.strip();
                String[] childrenData = fieldValue.split(",");
                this.children = new String[childrenData.length];
                for (int i = 0; i < childrenData.length; i++) {
                    this.children[i] = childrenData[i].strip();
                }
                continue;
            }
            if (fieldTitle.equals(NOTES_TITLE)) {
                this.notes = fieldValue;
                continue;
            }
            if (fieldTitle.equals(FATE_TITLE)) {
                this.fate = fieldValue;
                continue;
            }
            return false;
        }
        this.isValid = true;
        return true;
    }

    /**
     * Constructor de un objeto Lord, vacío.
     */
    public Lord() {
        this.isValid = false;
    }

    /**
     * Constructor de un objeto Lord.
     * 
     * @param json El json con los datos del Lord.
     * @throws RuntimeException Si el json no es correcto
     */
    public Lord(String json) {
        this.isValid = false;
        if (!this.parse(json)) {
            this.isValid = false;
            return;
        }
        this.isValid = true;
    }

    /**
     * Compara si el Lord es igual al pasado como parámetro.
     * 
     * @param lord
     * @return
     */
    public boolean equals(Lord lord) {
        if (lord == null) {
            return false;
        }
        if (this.alias != null && lord.alias != null) {
            if (Lord.comparadorAlias.compareTo(this, lord) == 0) {
                return true;
            }
        }
        if (Lord.comparadorUnicoNombre.compareTo(this, lord) == 0) {
            return true;
        }
        return false;
    }

    /**
     * Devuelve un String con los datos del Lord.
     */
    public String completeDataToString() {
        String txt = "";
        if (!this.isValid) {
            txt = Lord.LORD_TITLE + ": null\n";
            return txt;
        }
        txt = Lord.LORD_TITLE + ": " + this.fullName + "\n";
        if (this.numeral != null) {
            txt += "\t" + Lord.NUMERAL_TITLE + ": " + this.numeral + "\n";
        }
        txt += "\t" + Lord.HOUSE_TITLE + ": " + this.house + "\n";
        if (this.father != null) {
            txt += "\t" + Lord.FATHER_TITLE + ": " + this.father + "\n";
        } else {
            txt += "\t" + Lord.FATHER_TITLE + ": " + UNKNOWN_TITLE.substring(1, UNKNOWN_TITLE.length() - 1) + "\n";
        }
        if (this.mother != null) {
            txt += "\t" + Lord.MOTHER_TITLE + ": " + this.mother + "\n";
        }
        if (this.alias != null) {
            txt += "\t" + Lord.ALIAS_TITLE + ": " + this.alias + "\n";
        }
        if (this.title != null) {
            txt += "\t" + Lord.TITLE_TITLE + ": " + this.title + "\n";
        }
        if (this.wedTo != null) {
            txt += "\t" + Lord.WED_TO_TITLE + ": " + this.wedTo + "\n";
        }
        txt += "\t" + Lord.EYES_COLOR_TITLE + ": " + this.eyesColor + "\n";
        txt += "\t" + Lord.HAIR_COLOR_TITLE + ": " + this.hairColor + "\n";
        if (this.children != null && this.children.length > 0) {
            txt += "\t" + Lord.CHILDREN_TITLE + ": \n";
            for (int i = 0; i < this.children.length; i++) {
                txt += "\t" + "\t" + (i + 1) + ". ";
                txt += this.children[i] + "\n";
            }
        }
        if (this.notes != null) {
            txt += "\t" + Lord.NOTES_TITLE + ": " + this.notes + "\n";
        }
        if (this.fate != null) {
            txt += "\t" + Lord.FATE_TITLE + ": " + this.fate + "\n";
        }
        return txt;
    }

    /**
     * Devuelve un String con el nombre del Lord
     */
    public String toString() {
        return this.uniqueName;
    }

    /**
     * Solo para hacer algunas pruebas.
     * 
     * @param args
     */
    public static void main(String[] args) {
    }

}