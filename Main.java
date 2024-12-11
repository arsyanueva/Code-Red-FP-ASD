import java.util.*;

public class Main {
    private static GraphMap graphMap;
    private static Map<String, Location> locations;
    private static GameState gameState;
    private static Scanner scanner;

    public static void main(String[] args) {
        initializeGame();
        playGame();
    }

    private static void initializeGame() {
        graphMap = new GraphMap();
        locations = new HashMap<>();
        gameState = new GameState();
        scanner = new Scanner(System.in);
        initializeGraph();
        initializeLocations();
        displayIntroduction();
    }
    private static void initializeGraph() {
        // Adding edges based on the relationships
        graphMap.addEdge("Sistem Informasi", "Dr. Angka");
        graphMap.addEdge("Sistem Informasi", "Kedokteran");
        graphMap.addEdge("Sistem Informasi", "Research Center");
        graphMap.addEdge("Sistem Informasi", "Perpustakaan");
        graphMap.addEdge("Kedokteran", "Teknik Kelautan");
        graphMap.addEdge("Kedokteran", "Teknik Geomatika");
        graphMap.addEdge("Teknik Geomatika", "Teknik Informatika");
        graphMap.addEdge("Teknik Kelautan", "Teknik Informatika");
        graphMap.addEdge("Dr. Angka", "Perpustakaan");
        graphMap.addEdge("Perpustakaan", "Research Center");
        graphMap.addEdge("Research Center", "Teknik Informatika");
    }

    private static void displayIntroduction() {
        System.out.println("=== CODE RED: Murder Mystery ===\n" +//
        "Pada suatu hari di Departemen Sistem Informasi, sebuah tragedi tak terduga terjadi.\n" + //
        "Joko, seorang mahasiswa Sistem Informasi ditemukan tewas di gudang departemen dalam \n" + //
        "keadaan mengenaskan dimana tubuh korban tergantung dengan kepala yang terikat tali.");
        System.out.println("\n"+//
        "Joko bukan mahasiswa biasa. Dia adalah seorang mahasiswa yang sangat pintar, ambisius, dan terkenal baik\n" + //
        "oleh teman-temannya. Karena kepintarannya, Joko menjadi orang yang terlibat dalam sebuah penelitian \n" +// 
        "rahasia yang diindikasi penelitian terlarang. Sebelum kejadian, Joko dilanda kegelisahan karena dokumen penelitian \n" +//
        "yang seharusnya ia bawa hilang secara misterius. Ia juga menerima sebuah panggilan misterius. \n"+//
        "Banyak orang yang memiliki keterkaitan dengan Joko, namun sebuah surat petunjuk ada didekatnya\n"+//
        "'Hanya aku sang pemenang' \n"+//
        "Apa yang sebenarnya terjadi malam itu? Apakah ini kecelakaan, bunuh diri, atau sebuah pembunuhan yang direncanakan?");
        System.out.print("\n"+//
        "Sebagai seorang detektif, kamu ditugaskan untuk mengungkap apa yang sebenarnya terjadi. \n"+//
        "Setiap lokasi di kampus ini menyimpan petunjuk dan banyaknya pihak yang terlibat sebelum kematian Joko.\n"+//
        "Tugasmu adalah menghubungkan potongan-potongan ini, menyusun fakta dari tiap lokasi dan kesaksian, \n" + //
        "dan menemukan dalang di balik tragedi ini.\n" + //
        "Siapkah kamu memulai petualangan ini?");
    }

    private static void playGame() {
        while (!gameState.isGameOver()) {
            System.out.println();
            displayCurrentLocation();
            displayMenu();
            processChoice();
        }
        scanner.close();
    }

    private static void displayCurrentLocation() {
        System.out.println("\nLokasi saat ini: " + gameState.getCurrentLocation());
        if (!gameState.hasVisitedLocation(gameState.getCurrentLocation())) {
            System.out.println("Ini adalah lokasi pertama kali yang kamu kunjungi.");
        }
    }

    private static void displayMenu() {
        System.out.println("\nApa yang ingin kamu lakukan?");
        System.out.println("1. Introgasi saksi");
        System.out.println("2. Lihat barang bukti");
        System.out.println("3. Review clue");
        System.out.println("4. Pindah ke tempat lain");
        System.out.println("5. Tebak pembunuh");
        System.out.println("6. Exit game");
    }

    private static void processChoice() {
        int choice = getValidChoice(1, 6);
        switch (choice) {
            case 1:
                interrogateWitness();
                break;
            case 2:
                examineEvidence();
                break;
            case 3:
                reviewClues();
                break;
            case 4:
                moveLocation();
                break;
            case 5:
                makeAccusation();
                break;
            case 6:
                exitGame();
                break;
        }
    }

    private static int getValidChoice(int min, int max) {
        int choice;
        while (true) {
            System.out.println("Mau pilih nomor berapa, nih? (" + min + "-" + max + "): ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice >= min && choice <= max) {
                    return choice;
                }
                System.out.println("Masukkan angka antara " + min + " and " + max);
            } catch (NumberFormatException e) {
                System.out.println("Masukkan angka yang valid");
            }
        }
    }

    private static void interrogateWitness() {
        Location currentLocation = locations.get(gameState.getCurrentLocation());
        if (currentLocation.getWitness() != null) {
            Witness witness = currentLocation.getWitness();
            System.out.println("\n=== Introgasi Saksi ===" + witness.getName() + " ===");
            System.out.println("Deskripsi karakter: " + witness.getCharacterDesc());
            System.out.println("Pernyataan: " + witness.getStatement());
            gameState.addClue(witness.getStatement(), witness.getSort());  // Changed from getOrder() to getSort()
            gameState.visitLocation(gameState.getCurrentLocation());
        } else {
            System.out.println("Tidak ada saksi di lokasi ini.");
        }
    }

    private static void examineEvidence() {
        Location currentLocation = locations.get(gameState.getCurrentLocation());
        System.out.println("\n=== Lihat Barang Bukti ===");
        System.out.println("Barang bukti: " + currentLocation.getEvidence());
    }

    private static void reviewClues() {
        List<Sorting.ClueData> clues = gameState.getCollectedClues();
        if (clues.isEmpty()) {
            System.out.println("\nTidak ada clue yang dikumpulkan.");
            return;
        }
    
        // Sort and print the clues directly since they're already in ClueData format
        Sorting.printSortedClues(clues);
    }

    private static void moveLocation() {
        List<String> availableLocations = graphMap.getNeighbors(gameState.getCurrentLocation());
        System.out.println("\n=== Lokasi Selanjutnya ===");
        for (int i = 0; i < availableLocations.size(); i++) {
            System.out.println((i + 1) + ". " + availableLocations.get(i));
        }

        System.out.print("\nPilih lokasi (1-" + availableLocations.size() + "): ");
        int choice = getValidChoice(1, availableLocations.size());
        gameState.setCurrentLocation(availableLocations.get(choice - 1));
    }

    private static void makeAccusation() {
        System.out.println("\n=== Tebak Pembunuh ===");
        System.out.println("WARNING: Apabila kamu salah menangkap, pembunuh akan terus berkeliaran!");
        System.out.println("\nTersangka:");
        
        Map<Integer, String> suspects = new LinkedHashMap<>();
        int index = 1;
        for (Location location : locations.values()) {
            Witness witness = location.getWitness();
            if (witness != null && !witness.getName().equals("Bu Ita") && !witness.getName().equals("Pak Budi")) {
                System.out.println(index + ". " + witness.getName() + " - " + witness.getCharacterDesc());
                suspects.put(index++, witness.getName());
            }
        }
        System.out.println("0. Batalkan");
        int choice = getValidChoice(0, suspects.size());
        
        if (choice == 0) {
            System.out.println("Pemilihan dibatalkan.");
            return;
        }
    
        String accusedPerson = suspects.get(choice);
        System.out.println("\nPilihanmu adalah " + accusedPerson + ".");
        System.out.print("Apakah kamu yakin? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (confirm.equals("y")) {
            if (accusedPerson.equals("Saka")) {
                System.out.println("\n=== Case Solved! ===");
                System.out.println("Selamat! Kamu berhasil menangkap pembunuh.");
                System.out.println("\nRingkasan kejadian:");
                System.out.println("Saka membunuh Joko karena rasa iri dengan keberhasilan Joko, sehingga ada petunjuk tulisan 'Hanya aku sang pemenang'");
                System.out.println("\nPenjelasan lebih rinci");
                System.out.println("1. Argumen Saka untuk belajar bersama aneh karena Joko sedang mengalami kegelisahan");
                System.out.println("2. Pak Santoso bungkam karena takut penelitiannya terbongkar");
                System.out.println("3. Leo mencuri dokumen penelitian yang Joko bawa dan ingin mengancam untuk mengungkapkannya");
                System.out.println("4. Ari orang baik karena ingin membantu Joko menemukan dokumennya karena tahu Leo membawanya, Leo dan Ari sekelas");
                System.out.println("5. Fia mengkhawatirkan Joko dan ingin membantu, tetapi Joko menolak dan berpendapat bahwa permasalahan ini bukan masalah Fia");
                System.out.println("6. Bu Ita tidak sengaja berpapasan dengan Fia dan merupakan orang pertama yang menemukan Joko");
                System.out.println("7. Dua orang bertengkar yang terlihat di cctv adalah Ari dan Leo yang sedang meributkan dokumen penelitian");
                gameState.setGameOver(true);
            } else {
                System.out.println("\nKamu menangkap orang yang salah");
                System.out.println("Investigasi masih terus dilakukan. Hati-hati, kamu bisa jadi orang selanjutnya.");
            }
        }
    }

    private static void exitGame() {
        System.out.println("\nThank you for playing Code Red!");
        gameState.setGameOver(true);
    }
    private static void initializeLocations() {
        locations = new HashMap<>();
        
        // Initialize all locations with their witnesses and evidence
        locations.put("Kedokteran", new Location("Kedokteran", 
            new Witness("Beta","Teman penelitian Joko", "Sarung tangan Latex",1,  "Semalam dia bilang ingin mengambil sesuatu di departemennya"),
            "Sarung tangan Latex yang mencurigakan"));
    
        locations.put("Sistem Informasi", new Location("Sistem Informasi",
            new Witness("Bu Ita","Petugas Kebersihan Kampus",  "Tali dan bercak darah", 2, "Malam itu saya melihat perempuan dari arah yang sama"),
            "Tali dan bercak darah, CCTV yang pecah"));
    
        locations.put("Dr. Angka", new Location("Dr. Angka",
            new Witness("Pak Budi","Satpam kampus yang mengawasi cctv", "Rekaman CCTV", 3, "Melalui CCTV, saya melihat ada dua orang sedang bertengkar"),
            "Rekaman CCTV yang menunjukkan pertengkaran"));
        locations.put("Teknik Kelautan", new Location("Teknik Kelautan",
                new Witness("Leo", "Kakak tingkat yang sangat membenci Joko","Pipa air", 4, "Aku tidak menyangka dia pergi secepat itu, padahal aku ingin mengungkapkannya"),
                "Pipa air"));
        locations.put("Teknik Geomatika", new Location("Teknik Geomatika",
                new Witness("Ari", "Kakak tingkat sekaligus teman penelitian Joko","Saluran Air Bocor", 5, "Dia sepertinya khawatir karena ada dokumen yang hilang"),
                "Saluran Air Bocor"));
        locations.put("Teknik Informatika", new Location("Saka",
                new Witness("Saka","Teman Joko yang pintar", "Ketapel", 6, "Dia orang baik dan kami sering belajar bersama"),
                "Ketapel"));
        locations.put("Perpustakaan", new Location("Fia",
                new Witness("Fia", "Sahabat baik Joko yang sudah berteman selama 10 tahun","Sobekan buku", 7, "Dia dalam bahaya dan seharusnya aku tidak meninggalkannya"),
                "Sobekan buku"));
        locations.put("Research Center", new Location("Pak Santoso",
                new Witness("Pak Santoso", "Dosen pengampu penelitian","Dokumen Penelitian Terlarang", 8, "Saya tidak ingin berkomentar apapun"),
                "Dokumen Penelitian Terlarang"));

    }
    private static void sortClues(List<String> clues) {
        int n = clues.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // Assuming clues are strings with time information
                if (clues.get(j).compareTo(clues.get(j + 1)) > 0) {
                    // Swap
                    String temp = clues.get(j);
                    clues.set(j, clues.get(j + 1));
                    clues.set(j + 1, temp);
                }
            }
        }
    }
    private static void showClues(List<String> clues) {
        System.out.println("\nClues obtained:");
        for (String clue : clues) {
            System.out.println(clue);
        }
    }

}