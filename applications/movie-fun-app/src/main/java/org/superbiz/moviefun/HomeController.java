package org.superbiz.moviefun;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.superbiz.moviefun.moviesapi.*;

import java.util.Map;

@Controller
public class HomeController {

    private final MoviesClient moviesClient;
    private final AlbumsClient albumsClient;
    private final MovieFixtures movieFixtures;
    private final AlbumFixtures albumFixtures;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public HomeController(MoviesClient moviesClient, AlbumsClient albumsClient, MovieFixtures movieFixtures,
                          AlbumFixtures albumFixtures) {
        this.moviesClient = moviesClient;
        this.albumsClient = albumsClient;
        this.movieFixtures = movieFixtures;
        this.albumFixtures = albumFixtures;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/setup")
    public ModelAndView setup() {
        for (MovieInfo movie : movieFixtures.load()) {
            moviesClient.addMovie(movie);
        }

        for (AlbumInfo album : albumFixtures.load()) {
            albumsClient.addAlbum(album);
        }

        ModelAndView modelAndView = new ModelAndView("setup");
        Map<String, Object> model = modelAndView.getModel();
        model.put("movies", moviesClient.getMovies());
        model.put("albums", albumsClient.getAlbums());

        return modelAndView;
    }

    @GetMapping("/albums")
    public ModelAndView albums(Map<String, Object> model) {
        ModelAndView modelAndView = new ModelAndView("albums");
        modelAndView.getModel().put("albums", albumsClient.getAlbums());
        return modelAndView;
    }

    @GetMapping("/albums/{albumId}")
    public ModelAndView album(@PathVariable long albumId) {
        ModelAndView modelAndView = new ModelAndView("albumDetails");
        AlbumInfo album = albumsClient.find(albumId);
        modelAndView.getModel().put("album", album);

        return modelAndView;
    }
}
