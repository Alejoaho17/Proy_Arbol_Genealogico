/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proy_2_arb_gen;

/**
 * Clase que representa una Casa de Game of Thrones
 */
public class House {
    /**
     * Algunas constantes usadas en toString.
     */
    public final String HOUSE_TITLE = "House";

    /**
     * Lista de lords de la casa
     */
    ArrayList<Lord> lords = new ArrayList<>(15, 10);

    /**
     * El fullname y name de la casa.
     */
    public String fullname = null;
    public String name = null;

    /**
     * Parser de un objeto json con la información de la casa,
     * carga toda la información en los atributos que la representan
     * así como de los Lords que conforman la casa.
     * 
     * @param json el objeto json con la información de la casa
     */
    private void parser(String json) {
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

        // Separar por dos puntos, para obtener el nombre completo y el resto de la
        // información
        String[] data = json.split(":", 2);
        this.fullname = data[0].replace("\"", "").strip();
        this.name = this.fullname.split(" ", 2)[1];
        this.name = this.name.strip();

        // El resto del json para obtener los lords a partir de los objetos json de cada
        // uno.
        json = data[1].strip();

        // Quitamos los corchetes que abren y cierran.
        if (json.startsWith("[")) {
            json = json.substring(1, json.length());
        }
        if (json.endsWith("]")) {
            json = json.substring(0, json.length() - 1);
        }
        // Quitamos los espacios.
        json = json.strip();

        // Vamos a separar cada objeto json que representa a cada lord,
        // con un carácter especial, para luego poder hacer un split
        // y tener el json de cada lord.
        int numLLaves = 0;
        String jsonTxt = "";
        for (int i = 0; i < json.length(); i++) {
            if (json.charAt(i) == '{') {
                numLLaves++;
            }
            if (json.charAt(i) == '}') {
                numLLaves--;
            }
            if (json.charAt(i) == ',' && numLLaves == 0) {
                jsonTxt += "//";
                continue;
            }
            jsonTxt += json.charAt(i);
        }

        // Ahora que tenemos todos los json de cada lord, los vamos a
        // agregar a la lista de lords
        String[] jsonArray = jsonTxt.split("//");
        for (int i = 0; i < jsonArray.length; i++) {
            this.lords.agregar(new Lord(jsonArray[i]));
        }

        // Como la lista ya esta definida, compactamos para no tener espacios
        // vacíos en el array list.
        this.lords.compactar();
    }

    /**
     * Constructor de la clase.
     * 
     * @param json el json con la información de la casa.
     */
    public House(String json) {
        if (json == null) {
            throw new IllegalArgumentException("json no puede ser null");
        }
        if (json == "") {
            throw new IllegalArgumentException("json no puede ser vacío");
        }
        json = json.strip();
        json = json.strip();
        if (!json.startsWith("{")) {
            throw new IllegalArgumentException("Json must start with {");
        }
        if (!json.endsWith("}")) {
            throw new IllegalArgumentException("Json must end with }");
        }
        this.parser(json);
    }

    /**
     * Devuelve una representación en texto de la casa.
     */
    public String toString() {
        String txt = this.HOUSE_TITLE + ": " + this.name + "\n";
        for (int i = 0; i < this.lords.size(); i++) {
            String[] lines = this.lords.get(i).toString().split("\n");
            for (int j = 0; j < lines.length; j++) {
                txt += "\t" + lines[j] + "\n";
            }
            if (i < this.lords.size() - 1) {
                txt += "\n";
            }
        }
        return txt;
    }

    /**
     * Solo para hacer pruebas.
     * 
     * @param args
     */

    public static void main(String[] args) {
        String jsonHouse = "{\n" +
                "    \"House Baratheon\":[\n" +
                "        {\n" +
                "            \"Orys Baratheon\":[\n" +
                "                {\"Of his name\":\"First\"},\n" +
                "                {\"Born to\":\"[Unknown]\"},\n" +
                "                {\"Known throughout as\":\"Orys One-Hand\"},\n" +
                "                {\"Held title\":\"Hand of the King\"},\n" +
                "                {\"Wed to\":\"Argalia Durrandon\"},\n" +
                "                {\"Of eyes\":\"Brown\"},\n" +
                "                {\"Of hair\":\"Black\"},\n" +
                "                {\"Father to\":[\n" +
                "                        \"Axel\",\n" +
                "                        \"Serac\",\n" +
                "                        \"Mychal Raymont\",\n" +
                "                        \"Ethelide\",\n" +
                "                        \"Theresa\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\"Notes\":\"Founder of House Baratheon\"},\n" +
                "                {\"Fate\":\"Died defending King Aegon Targaryen, First of his name, from an uprising\"}\n"
                +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"Axel Baratheon\":[\n" +
                "                {\"Of his name\":\"First\"},\n" +
                "                {\"Born to\":\"Orys One-Hand\"},\n" +
                "                {\"Born to\":\"Argalia Durrandon\"},\n" +
                "                {\"Held title\":\"Lord Paramount of the Stormlands\"},\n" +
                "                {\"Wed to\":\"Alana Penrose\"},\n" +
                "                {\"Of eyes\":\"Brown\"},\n" +
                "                {\"Of hair\":\"Black\"},\n" +
                "                {\"Fate\":\"Died in a duel with Steffon Trant, who had dishonored his wife, and therefore had no children.\"}\n"
                +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
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
                "        },\n" +
                "        {\n" +
                "            \"Mychal Raymont Baratheon\":[\n" +
                "                {\"Of his name\":\"First\"},\n" +
                "                {\"Born to\":\"Orys One-Hand\"},\n" +
                "                {\"Born to\":\"Argalia Durrandon\"},\n" +
                "                {\"Held title\":\"Knight of the Kingsguard\"},\n" +
                "                {\"Of eyes\":\"Brown\"},\n" +
                "                {\"Of hair\":\"Black\"},\n" +
                "                {\"Fate\":\"Died in his thirtieth year from wounds sustained preventing an assassination attempt on his royal grace, King Aenys Targaryen, First of his name.\"}\n"
                +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"William Baratheon\":[\n" +
                "                {\"Of his name\":\"First\"},\n" +
                "                {\"Born to\":\"Serac Baratheon, First of his name\"},\n" +
                "                {\"Born to\":\"Monica Velaryon\"},\n" +
                "                {\"Held title\":\"Lord Paramount of the Stormlands\"},\n" +
                "                {\"Wed to\":\"Martyna Mullendore\"},\n" +
                "                {\"Of eyes\":\"Brown\"},\n" +
                "                {\"Of hair\":\"Black\"},\n" +
                "                {\"Father to\":[\n" +
                "                        \"William\",\n" +
                "                        \"Jon\",\n" +
                "                        \"Martyn\",\n" +
                "                        \"Mace\",\n" +
                "                        \"Orys\",\n" +
                "                        \"Theodor\",\n" +
                "                        \"Bryte\",\n" +
                "                        \"Tommax\",\n" +
                "                        \"Brune\",\n" +
                "                        \"Gude\",\n" +
                "                        \"Lex\",\n" +
                "                        \"Wineiri\",\n" +
                "                        \"Miriam\",\n" +
                "                        \"Argalia\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\"Fate\":\"Died of heart problems at sixty years old.\"}\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"William Baratheon\":[\n" +
                "                {\"Of his name\":\"Second\"},\n" +
                "                {\"Born to\":\"William Baratheon, First of his name\"},\n" +
                "                {\"Born to\":\"Martyna Mullendore\"},\n" +
                "                {\"Held title\":\"Lord Paramount of the Stormlands\"},\n" +
                "                {\"Of eyes\":\"Brown\"},\n" +
                "                {\"Of hair\":\"Black\"},\n" +
                "                {\"Father to\":[\n" +
                "                    \"Symeon\"\n" +
                "                    ]\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"Symeon Baratheon\":[\n" +
                "                {\"Of his name\":\"Second\"},\n" +
                "                {\"Born to\":\"William Baratheon, Second of his name\"},\n" +
                "                {\"Wed to\":\"Meera\"},\n" +
                "                {\"Held title\":\"Lord Paramount of the Stormlands\"},\n" +
                "                {\"Of eyes\":\"Brown\"},\n" +
                "                {\"Of hair\":\"Black\"},\n" +
                "                {\"Father to\":[\n" +
                "                    \"Lyonel\"\n" +
                "                    ]\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"Lyonel Baratheon\":[\n" +
                "                {\"Of his name\":\"First\"},\n" +
                "                {\"Born to\":\"Symeon Baratheon\"},\n" +
                "                {\"Born to\":\"Meera\"},\n" +
                "                {\"Known throughout as\":\"The Laughing Storm\"},\n" +
                "                {\"Held title\":\"Knight of the Seven Kingdoms\"},\n" +
                "                {\"Of eyes\":\"Brown\"},\n" +
                "                {\"Of hair\":\"Black\"},\n" +
                "                {\"Father to\":[\n" +
                "                        \"Steffon\"\n" +
                "                    ]\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"Steffon Baratheon\":[\n" +
                "                {\"Of his name\":\"Second\"},\n" +
                "                {\"Born to\":\"The Laughing Storm\"},\n" +
                "                {\"Of eyes\":\"Brown\"},\n" +
                "                {\"Of hair\":\"Black\"},\n" +
                "                {\"Father to\":[\n" +
                "                        \"Ormund\"\n" +
                "                    ]\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"Ormund Baratheon\":[\n" +
                "                {\"Of his name\":\"First\"},\n" +
                "                {\"Born to\":\"Steffon Baratheon, Second of his name\"},\n" +
                "                {\"Held title\":\"Hand of the King\"},\n" +
                "                {\"Of eyes\":\"Brown\"},\n" +
                "                {\"Of hair\":\"Black\"},\n" +
                "                {\"Father to\":[\n" +
                "                        \"Steffon\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\"Fate\":\"Died in his son's arms fighting at the Stepstones\"}\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"Steffon Baratheon\":[\n" +
                "                {\"Of his name\":\"Third\"},\n" +
                "                {\"Born to\":\"Ormund Baratheon, First of his name\"},\n" +
                "                {\"Wed to\":\"Cassandra Estermont\"},\n" +
                "                {\"Held title\":\"Lord Paramount of the Stormlands\"},\n" +
                "                {\"Of eyes\":\"Brown\"},\n" +
                "                {\"Of hair\":\"Black\"},\n" +
                "                {\"Father to\":[\n" +
                "                        \"Robert\",\n" +
                "                        \"Stannis\",\n" +
                "                        \"Renly\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\"Fate\":\"Died with his wife when their ship, the Windproud, sank in Shipbreaker Bay\"}\n"
                +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"Robert Baratheon\":[\n" +
                "                {\"Of his name\":\"First\"},\n" +
                "                {\"Born to\":\"Steffon Baratheon, Third of his name\"},\n" +
                "                {\"Known throughout as\":\"The Usurper\"},\n" +
                "                {\"Held title\":\"King of the Andals and the First Men, Lord of the Seven Kingdoms\"},\n"
                +
                "                {\"Wed to\":\"Cersei Lannister\"},\n" +
                "                {\"Of eyes\":\"Brown\"},\n" +
                "                {\"Of hair\":\"Black\"},\n" +
                "                {\"Father to\":[\n" +
                "                        \"Unnamed Son\",\n" +
                "                        \"Joffrey\",\n" +
                "                        \"Myrcella\",\n" +
                "                        \"Tommen\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\"Notes\":\"Seven known bastards, not recognized\"},\n" +
                "                {\"Fate\":\"Died as a result of a hunting accident\"}\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"Stannis Baratheon\":[\n" +
                "                {\"Of his name\":\"First\"},\n" +
                "                {\"Born to\":\"Steffon Baratheon, Third of his name\"},\n" +
                "                {\"Held title\":\"Lord of Dragonstone\"},\n" +
                "                {\"Wed to\":\"Selyse Florent\"},\n" +
                "                {\"Of eyes\":\"Brown\"},\n" +
                "                {\"Of hair\":\"Black\"},\n" +
                "                {\"Father to\":[\n" +
                "                        \"Shireen Baratheon\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\"Fate\":\"Died during the Battle in the Ice\"}\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"Renly Baratheon\":[\n" +
                "                {\"Of his name\":\"First\"},\n" +
                "                {\"Born to\":\"Steffon Baratheon, Third of his name\"},\n" +
                "                {\"Held title\":\"Lord Paramount of the Stormlands\"},\n" +
                "                {\"Of eyes\":\"Brown\"},\n" +
                "                {\"Of hair\":\"Black\"},\n" +
                "                {\"Fate\":\"Murdered in his chambers\"}\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"Joffrey Baratheon\":[\n" +
                "                {\"Of his name\":\"First\"},\n" +
                "                {\"Born to\":\"Robert Baratheon, First of his name\"},\n" +
                "                {\"Born to\":\"Cersei Lannister\"},\n" +
                "                {\"Held title\":\"King of the Andals and the First Men, Lord of the Seven Kingdoms\"},\n"
                +
                "                {\"Wed to\":\"Margaery Tyrell\"},\n" +
                "                {\"Of eyes\":\"Green\"},\n" +
                "                {\"Of hair\":\"Golden\"},\n" +
                "                {\"Notes\":\"At no point did Joffrey control all of the Seven Kingdoms\"},\n" +
                "                {\"Fate\":\"Poisoned by his own small council before consumating his marriage\"}\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"Tommen Baratheon\":[\n" +
                "                {\"Of his name\":\"First\"},\n" +
                "                {\"Born to\":\"Robert Baratheon, First of his name\"},\n" +
                "                {\"Born to\":\"Cersei Lannister\"},\n" +
                "                {\"Held title\":\"King of the Andals and the First Men, Lord of the Seven Kingdoms\"},\n"
                +
                "                {\"Wed to\":\"Margaery Tyrell\"},\n" +
                "                {\"Of eyes\":\"Green\"},\n" +
                "                {\"Of hair\":\"Golden\"},\n" +
                "                {\"Fate\":\"Died during the Destruction of the Great Sept of Baelor\"}\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        House house = new House(jsonHouse);
        System.out.println(house.toString());
    }
}
