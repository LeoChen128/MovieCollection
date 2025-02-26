import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MovieCollection
{
    private ArrayList<Movie> movies;
    private Scanner scanner;
    private ArrayList<String> allCastMembers;
    private ArrayList<String> allGenres;


    public MovieCollection(String fileName)
    {
        importMovieList(fileName);
        scanner = new Scanner(System.in);
        castAndGenre();
    }

    public ArrayList<Movie> getMovies()
    {
        return movies;
    }

    private void sortStringList(ArrayList<String> list) {
        for (int j = 1; j < list.size(); j++) {
            String temp = list.get(j);

            int possibleIndex = j;
            while (possibleIndex > 0 && temp.compareTo(list.get(possibleIndex - 1)) < 0) {
                list.set(possibleIndex, list.get(possibleIndex - 1));
                possibleIndex--;
            }
            list.set(possibleIndex, temp);
        }
    }

    private void castAndGenre() {
        allCastMembers = new ArrayList<>();
        allGenres = new ArrayList<>();

        for (Movie movie : movies) {
            String[] castMembers = movie.getCast().split("\\|");
            for (String member : castMembers) {
                String castMember = member.trim(); // in case of a space
                if (!allCastMembers.contains(castMember)) {
                    allCastMembers.add(castMember);
                }
            }

            String[] movieGenres = movie.getGenres().split("\\|");
            for (String movieGenre : movieGenres) {
                String genre = movieGenre.trim();
                if (!allGenres.contains(genre)) {
                    allGenres.add(genre);
                }
            }
        }

        sortStringList(allCastMembers);
        sortStringList(allGenres);
    }

    public void menu()
    {
        String menuOption = "";

        System.out.println("Welcome to the movie collection!");
        System.out.println("Total: " + movies.size() + " movies");

        while (!menuOption.equals("q"))
        {
            System.out.println("------------ Main Menu ----------");
            System.out.println("- search (t)itles");
            System.out.println("- search (k)eywords");
            System.out.println("- search (c)ast");
            System.out.println("- see all movies of a (g)enre");
            System.out.println("- list top 50 (r)ated movies");
            System.out.println("- list top 50 (h)igest revenue movies");
            System.out.println("- (q)uit");
            System.out.print("Enter choice: ");
            menuOption = scanner.nextLine();

            if (!menuOption.equals("q"))
            {
                processOption(menuOption);
            }
        }
    }

    private void processOption(String option)
    {
        if (option.equals("t"))
        {
            searchTitles();
        }
        else if (option.equals("c"))
        {
            searchCast();
        }
        else if (option.equals("k"))
        {
            searchKeywords();
        }
        else if (option.equals("g"))
        {
            listGenres();
        }
        else if (option.equals("r"))
        {
            listHighestRated();
        }
        else if (option.equals("h"))
        {
            listHighestRevenue();
        }
        else
        {
            System.out.println("Invalid choice!");
        }
    }

    private void searchTitles()
    {
        System.out.print("Enter a title search term: ");
        String searchTerm = scanner.nextLine();

        // prevent case sensitivity
        searchTerm = searchTerm.toLowerCase();

        // arraylist to hold search results
        ArrayList<Movie> results = new ArrayList<Movie>();

        // search through ALL movies in collection
        for (int i = 0; i < movies.size(); i++)
        {
            String movieTitle = movies.get(i).getTitle();
            movieTitle = movieTitle.toLowerCase();

            if (movieTitle.indexOf(searchTerm) != -1)
            {
                //add the Movie objest to the results list
                results.add(movies.get(i));
            }
        }

        // sort the results by title
        sortResults(results);

        // now, display them all to the user
        for (int i = 0; i < results.size(); i++)
        {
            String title = results.get(i).getTitle();

            // this will print index 0 as choice 1 in the results list; better for user!
            int choiceNum = i + 1;

            System.out.println("" + choiceNum + ". " + title);
        }

        System.out.println("Which movie would you like to learn more about?");
        System.out.print("Enter number: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        Movie selectedMovie = results.get(choice - 1);

        displayMovieInfo(selectedMovie);

        System.out.println("\n ** Press Enter to Return to Main Menu **");
        scanner.nextLine();
    }

    private void sortResults(ArrayList<Movie> listToSort)
    {
        for (int j = 1; j < listToSort.size(); j++)
        {
            Movie temp = listToSort.get(j);
            String tempTitle = temp.getTitle();

            int possibleIndex = j;
            while (possibleIndex > 0 && tempTitle.compareTo(listToSort.get(possibleIndex - 1).getTitle()) < 0)
            {
                listToSort.set(possibleIndex, listToSort.get(possibleIndex - 1));
                possibleIndex--;
            }
            listToSort.set(possibleIndex, temp);
        }
    }

    private void displayMovieInfo(Movie movie)
    {
        System.out.println();
        System.out.println("Title: " + movie.getTitle());
        System.out.println("Tagline: " + movie.getTagline());
        System.out.println("Runtime: " + movie.getRuntime() + " minutes");
        System.out.println("Year: " + movie.getYear());
        System.out.println("Directed by: " + movie.getDirector());
        System.out.println("Cast: " + movie.getCast());
        System.out.println("Overview: " + movie.getOverview());
        System.out.println("User rating: " + movie.getUserRating());
        System.out.println("Box office revenue: " + movie.getRevenue());
    }

    private void searchCast()
    {
        System.out.print("Enter a cast member name: ");
        String searchTerm = scanner.nextLine().toLowerCase();
        ArrayList<String> results = new ArrayList<>();

        for (String allCastMember : allCastMembers) {
            if (allCastMember.toLowerCase().indexOf(searchTerm) != -1) {
                results.add(allCastMember);
            }
        }

        if (results.size() == 0) {
            System.out.println("Cast member not found.");
            System.out.println("\n ** Press Enter to Return to Main Menu **");
            scanner.nextLine();
            return;
        }

        sortStringList(results);

        for (int i = 0; i < results.size(); i++) {
            int choiceNum = i + 1;
            System.out.println("" + choiceNum + ". " + results.get(i));
        }

        System.out.println("Which cast member would you like to learn more about?");
        System.out.print("Enter number: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        String selectedCastMember = results.get(choice - 1);

        ArrayList<Movie> castMovies = new ArrayList<>();
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getCast().contains(selectedCastMember)) {
                castMovies.add(movies.get(i));
            }
        }

        sortResults(castMovies);

        System.out.println("\nMovies starring " + selectedCastMember + ":");
        for (int i = 0; i < castMovies.size(); i++) {
            int choiceNum = i + 1;
            System.out.println("" + choiceNum + ". " + castMovies.get(i).getTitle());
        }

        System.out.println("Which movie would you like to learn more about?");
        System.out.print("Enter number: ");

        choice = scanner.nextInt();
        scanner.nextLine();

        Movie selectedMovie = castMovies.get(choice - 1);
        displayMovieInfo(selectedMovie);

        System.out.println("\n ** Press Enter to Return to Main Menu **");
        scanner.nextLine();
    }

    private void searchKeywords()
    {
        System.out.print("Enter a keyword search term: ");
        String searchTerm = scanner.nextLine();

        searchTerm = searchTerm.toLowerCase();

        ArrayList<Movie> result = new ArrayList<Movie>();

        for (Movie movie : movies) {
            String movieKeywords = movie.getKeywords();
            movieKeywords = movieKeywords.toLowerCase();

            if (movieKeywords.indexOf(searchTerm) != -1) {
                result.add(movie);
            }
        }

        if (result.size() == 0) {
            System.out.println("No movies match that keyword!");
            System.out.println("\n ** Press Enter to Return to Main Menu **");
            scanner.nextLine();
            return;
        }
        sortResults(result);

        for (int i = 0; i < result.size(); i++)
        {
            String title = result.get(i).getTitle();

            int choiceNum = i + 1;

            System.out.println("" + choiceNum + ". " + title);
        }

        System.out.println("Which movie would you like to learn more about?");
        System.out.print("Enter number: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        Movie movieChoice = result.get(choice - 1);

        displayMovieInfo(movieChoice);

        System.out.println("\n ** Press Enter to Return to Main Menu **");
        scanner.nextLine();
    }

    private void listGenres()
    {
        for (int i = 0; i < allGenres.size(); i++) {
            int choiceNum = i + 1;
            System.out.println("" + choiceNum + ". " + allGenres.get(i));
        }

        System.out.println("Which genre do you want to choose?: ");
        System.out.print("Enter number: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        String selectedGenre = allGenres.get(choice - 1);

        ArrayList<Movie> genreMovies = new ArrayList<>();
        for (Movie movie : movies) {
            String[] movieGenres = movie.getGenres().split("\\|");
            for (int j = 0; j < movieGenres.length; j++) {
                if (movieGenres[j].trim().equals(selectedGenre)) {
                    genreMovies.add(movie);
                    j = movieGenres.length;
                }
            }
        }

        sortResults(genreMovies);

        System.out.println("\nMovies in genre: " + selectedGenre);
        for (int i = 0; i < genreMovies.size(); i++) {
            int choiceNum = i + 1;
            System.out.println("" + choiceNum + ". " + genreMovies.get(i).getTitle());
        }

        System.out.println("Which movie would you like to learn more about?");
        System.out.print("Enter number: ");

        choice = scanner.nextInt();
        scanner.nextLine();

        Movie selectedMovie = genreMovies.get(choice - 1);
        displayMovieInfo(selectedMovie);

        System.out.println("\n ** Press Enter to Return to Main Menu **");
        scanner.nextLine();
    }

    private void listHighestRated()
    {
        ArrayList<Movie> ratedMovies = new ArrayList<>();
        for (Movie value : movies) {
            ratedMovies.add(value);
        }

        for (int i = 0; i < ratedMovies.size() - 1; i++) {
            for (int j = 0; j < ratedMovies.size() - i - 1; j++) {
                if (ratedMovies.get(j).getUserRating() < ratedMovies.get(j + 1).getUserRating()) {
                    Movie temp = ratedMovies.get(j);
                    ratedMovies.set(j, ratedMovies.get(j + 1));
                    ratedMovies.set(j + 1, temp);
                }
            }
        }

        System.out.println("\nTop 50 Highest Rated Movies:");

        for (int i = 0; i < 50; i++) {
            Movie movie = ratedMovies.get(i);
            int choiceNum = i + 1;
            System.out.println("" + choiceNum + ". " + movie.getTitle() + ": " + movie.getUserRating());
        }

        System.out.println("Which movie would you like to learn more about?");
        System.out.print("Enter number: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        Movie selectedMovie = ratedMovies.get(choice - 1);
        displayMovieInfo(selectedMovie);

        System.out.println("\n ** Press Enter to Return to Main Menu **");
        scanner.nextLine();
    }

    private void listHighestRevenue()
    {
        ArrayList<Movie> revenueMovies = new ArrayList<>();
        for (Movie value : movies) {
            revenueMovies.add(value);
        }

        for (int i = 0; i < revenueMovies.size() - 1; i++) {
            for (int j = 0; j < revenueMovies.size() - i - 1; j++) {
                if (revenueMovies.get(j).getRevenue() < revenueMovies.get(j + 1).getRevenue()) {
                    Movie temp = revenueMovies.get(j);
                    revenueMovies.set(j, revenueMovies.get(j + 1));
                    revenueMovies.set(j + 1, temp);
                }
            }
        }

        System.out.println("\nTop 50 Highest Revenue Movies:");

        for (int i = 0; i < 50; i++) {
            Movie movie = revenueMovies.get(i);
            int choiceNum = i + 1;
            System.out.println("" + choiceNum + ". " + movie.getTitle() + ": $" + movie.getRevenue());
        }

        System.out.println("Which movie would you like to learn more about?");
        System.out.print("Enter number: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        Movie selectedMovie = revenueMovies.get(choice - 1);
        displayMovieInfo(selectedMovie);

        System.out.println("\n ** Press Enter to Return to Main Menu **");
        scanner.nextLine();
    }

    private void importMovieList(String fileName)
    {
        try
        {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();

            movies = new ArrayList<Movie>();

            while ((line = bufferedReader.readLine()) != null)
            {
                String[] movieFromCSV = line.split(",");

                String title = movieFromCSV[0];
                String cast = movieFromCSV[1];
                String director = movieFromCSV[2];
                String tagline = movieFromCSV[3];
                String keywords = movieFromCSV[4];
                String overview = movieFromCSV[5];
                int runtime = Integer.parseInt(movieFromCSV[6]);
                String genres = movieFromCSV[7];
                double userRating = Double.parseDouble(movieFromCSV[8]);
                int year = Integer.parseInt(movieFromCSV[9]);
                int revenue = Integer.parseInt(movieFromCSV[10]);

                Movie nextMovie = new Movie(title, cast, director, tagline, keywords, overview, runtime, genres, userRating, year, revenue);
                movies.add(nextMovie);
            }
            bufferedReader.close();
        }
        catch(IOException exception)
        {
            // Print out the exception that occurred
            System.out.println("Unable to access " + exception.getMessage());
        }
    }
}