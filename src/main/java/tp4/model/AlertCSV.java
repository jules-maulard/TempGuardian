package tp4.model;

public class AlertCSV implements IAlertOutput {

    // nom fichier CSV
    public static final String ALERTS_CSV_FILE;

    public AlertCSV(UserConfig user) {
        ALERTS_CSV_FILE = "alerts_" + user.hashCode() + ".csv";
        // creer le fichier s'il n'existe pas
        if (!java.nio.file.Files.exists(java.nio.file.Paths.get(ALERTS_CSV_FILE))) {
            try {
                java.nio.file.Files.createFile(java.nio.file.Paths.get(ALERTS_CSV_FILE));
                // ecrire l'entete
                java.nio.file.Files.write(java.nio.file.Paths.get(ALERTS_CSV_FILE),
                        "Type,Releve,Threshold,Timestamp,Address\n".getBytes(),
                        java.nio.file.StandardOpenOption.APPEND);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void saveAlert(String type, double releve, double threshold, Date timestamp, String address) {
        String line = String.format("%s,%.2f,%.2f,%s,%s\n",
                type, releve, threshold, timestamp.toString(), address);
        try {
            java.nio.file.Files.write(java.nio.file.Paths.get(ALERTS_CSV_FILE),
                    line.getBytes(),
                    java.nio.file.StandardOpenOption.APPEND);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

}
