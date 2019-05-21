package org.superbiz.moviefun.moviesapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;

public class AlbumsClient {

    private String albumsUrl;
    private RestOperations restOperations;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static ParameterizedTypeReference<List<AlbumInfo>> movieListType = new ParameterizedTypeReference<List<AlbumInfo>>() {
    };
    private static ParameterizedTypeReference<AlbumInfo> albumType = new ParameterizedTypeReference<AlbumInfo>() {
    };


    public AlbumsClient(String albumsUrl, RestOperations restOperations) {
        this.albumsUrl = albumsUrl;
        this.restOperations = restOperations;
    }

    public void addAlbum(AlbumInfo movie) {
        logger.debug("Album: " + movie.toString());
        restOperations.postForEntity(albumsUrl, movie, AlbumInfo.class);

    }

    public void deleteMovieId(Long movieId) {
        restOperations.delete(albumsUrl + "/" + movieId);
    }

    public int countAll() {
        return restOperations.getForObject(albumsUrl + "/count", Integer.class);
    }


    public int count(String field, String key) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(albumsUrl + "/count")
                .queryParam("field", field)
                .queryParam("key", key);

        return restOperations.getForObject(builder.toUriString(), Integer.class);
    }


    public List<AlbumInfo> findAll(int start, int pageSize) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(albumsUrl)
                .queryParam("start", start)
                .queryParam("pageSize", pageSize);

        return restOperations.exchange(builder.toUriString(), GET, null, movieListType).getBody();
    }

    public List<AlbumInfo> findRange(String field, String key, int start, int pageSize) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(albumsUrl)
                .queryParam("field", field)
                .queryParam("key", key)
                .queryParam("start", start)
                .queryParam("pageSize", pageSize);

        return restOperations.exchange(builder.toUriString(), GET, null, movieListType).getBody();
    }

    public AlbumInfo find(long id) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(albumsUrl).path("/" + String.valueOf(id));
        return restOperations.exchange(builder.toUriString(), GET, null, albumType).getBody();
    }

    public List<AlbumInfo> getAlbums() {
        return restOperations.exchange(albumsUrl, GET, null, movieListType).getBody();
    }
}