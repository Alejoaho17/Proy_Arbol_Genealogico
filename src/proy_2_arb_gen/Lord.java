/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

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

    public static IComparador<Lord> comparadorNombre = (Lord a, Lord b) -> a.name.toLowerCase()
            .compareTo(b.name.toLowerCase());
    public static IComparador<Lord> comparadorUnicoNombre = (Lord a, Lord b) -> {
        String numeral1 = (a.numeral == null) ? "" : a.numeral;
        String numeral2 = (b.numeral == null) ? "" : b.numeral;
        return (a.name + numeral1).toLowerCase().compareTo((b.name + numeral2).toLowerCase());
    };
    public static IComparador<Lord> comparadorAlias = (Lord a, Lord b) -> a.alias.toLowerCase()
            .compareTo(b.alias.toLowerCase());
    public static IComparador<Lord> comparadorTitle = (Lord a, Lord b) -> a.title.toLowerCase()
            .compareTo(b.title.toLowerCase());

    /**
     * La información del lord
     */
    public String fullname = null;
    public String name = null;
    public String lastname = null;
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
     * Parser de un objeto json con la información del lord,
     * carga toda la información en los atributos que la representan
     * 
     * @param json el objeto json con la información del lord
     */
    private void parser(String json) {
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
        fullname = data[0].strip();
        name = fullname.split(" ", 2)[0];
        lastname = fullname.split(" ", 2)[1];
        house = lastname;

        // Nos quedamos con el resto de la información
        json = data[1];
        // Volvemos a quitar los espacios iniciales y finales
        json = json.strip();
        if (json.startsWith("[")) {
            json = json.substring(1, json.length());
        }
        if (json.endsWith("]")) {
            json = json.substring(0, json.length() - 1);
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
            }
            if (field.endsWith("}")) {
                field = field.substring(0, field.length() - 1);
            }

            // obtenemos el nombre del campo y el valor del campo
            String[] fieldData = field.split(":", 2);
            // le quitamos espacios iniciales y finales
            String fieldTitle = fieldData[0].strip();
            String fieldValue = fieldData[1].strip();

            // ahora, vamos a ir viendo los campos y sus valores
            if (fieldTitle.equals(NUMERAL_TITLE)) {
                this.numeral = fieldValue;
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
        }
    }

    /**
     * Constructor de un objeto Lord.
     * 
     * @param json El json con los datos del Lord.
     */
    public Lord(String json) {
        if (json == null) {
            throw new IllegalArgumentException("Json cannot be null");
        }
        if (json == "") {
            throw new IllegalArgumentException("Json cannot be empty");
        }
        json = json.strip();
        if (!json.startsWith("{")) {
            throw new IllegalArgumentException("Json must start with {");
        }
        if (!json.endsWith("}")) {
            throw new IllegalArgumentException("Json must end with }");
        }
        this.parser(json);
    }

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
    public String toString() {
        String txt = "";
        txt = this.LORD_TITLE + ": " + this.fullname + "\n";
        if (this.numeral != null) {
            txt += "\t" + this.NUMERAL_TITLE + ": " + this.numeral + "\n";
        }
        txt += "\t" + this.HOUSE_TITLE + ": " + this.house + "\n";
        if (this.father != null) {
            txt += "\t" + this.FATHER_TITLE + ": " + this.father + "\n";
        } else {
            txt += "\t" + this.FATHER_TITLE + ": " + UNKNOWN_TITLE.substring(1, UNKNOWN_TITLE.length() - 1) + "\n";
        }
        if (this.mother != null) {
            txt += "\t" + this.MOTHER_TITLE + ": " + this.mother + "\n";
        }
        if (this.alias != null) {
            txt += "\t" + this.ALIAS_TITLE + ": " + this.alias + "\n";
        }
        if (this.title != null) {
            txt += "\t" + this.TITLE_TITLE + ": " + this.title + "\n";
        }
        if (this.wedTo != null) {
            txt += "\t" + this.WED_TO_TITLE + ": " + this.wedTo + "\n";
        }
        txt += "\t" + this.EYES_COLOR_TITLE + ": " + this.eyesColor + "\n";
        txt += "\t" + this.HAIR_COLOR_TITLE + ": " + this.hairColor + "\n";
        if (this.children != null && this.children.length > 0) {
            txt += "\t" + this.CHILDREN_TITLE + ": \n";
            for (int i = 0; i < this.children.length; i++) {
                txt += "\t" + "\t" + (i + 1) + ". ";
                txt += this.children[i] + "\n";
            }
        }
        if (this.notes != null) {
            txt += "\t" + this.NOTES_TITLE + ": " + this.notes + "\n";
        }
        if (this.fate != null) {
            txt += "\t" + this.FATE_TITLE + ": " + this.fate + "\n";
        }
        return txt;
    }

    /**
     * Solo para hacer algunas pruebas.
     * 
     * @param args
     */
    public static void main(String[] args) {
        String jsonTxt = "{\n" +
                "            \"Serac Baratheon\":[\n" +
                "                {\"Of his name\":\"First\"},\n" +
                "                {\"Born to\":\"Orys One-Hand\"},\n" +
                "                {\"Born to\":\"Argalia Durrandon\"},\n" +
                "                {\"Held title\":\"Lord Paramount of the Stormlands\"},\n" +
                "                {\"Wed to\":\"Monica Velaryon\"},\n" +
                "                {\"Of eyes\":\"Brown\"},\n" +
                "                {\"Of hair\":\"Black\"},\n" +
                "                {\"Father to\":[\n" +
                "                        \"Reginald\",\n" +
                "                        \"William\",\n" +
                "                        \"Steffon\",\n" +
                "                        \"Padraic\",\n" +
                "                        \"Flynn\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\"Notes\":\"Inherited title upon death of brother.\"}\n" +
                "            ]\n" +
                "        }";
        Lord lord = new Lord(jsonTxt);
        System.out.println(lord.toString());
    }

}
