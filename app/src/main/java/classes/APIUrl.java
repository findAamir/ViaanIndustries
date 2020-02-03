package classes;

public class APIUrl {
    public String domain_url ;
    public String url(){
        final String APIKEY = "626ab80688886066a7aeda8b5a72195b";
        return domain_url="https://api.themoviedb.org/3/movie/now_playing?api_key="+APIKEY+"";

    }
}
