package restling.restlet

import groovy.transform.CompileStatic
import org.restlet.Context
import org.restlet.Request
import org.restlet.Response
import org.restlet.data.MediaType
import org.restlet.data.Preference
import org.restlet.routing.Filter

/**
 * A Restlet {@link Filter} that sets the REST API preference for talking JSON, and falling back to XML. It will try
 * to do other types if they really insist.
 */
@CompileStatic
class MediaTypePreferenceFilter extends Filter {

    MediaTypePreferenceFilter(Context ctx) {
        super(ctx)
    }

    protected int beforeHandle(Request request, Response response) {
        assert response: "No response provided!"
        assert request: "No request provided!"
        def log = getLogger();
        assert log: "No logger provided!"

        def clientInfo = request.clientInfo
        assert clientInfo: "No client info available on the request!"

        def acceptedMediaTypes = clientInfo.acceptedMediaTypes

        if (!acceptedMediaTypes || acceptedMediaTypes.empty) {
            log.fine("Media type is empty; setting to JSON");
            clientInfo.accept(MediaType.APPLICATION_JSON);
        } else if (acceptedMediaTypes.size() == 1 && acceptedMediaTypes[0].metadata?.equals(MediaType.ALL)) {
            log.fine("Media type is everything; setting to JSON");
            clientInfo.accept(MediaType.APPLICATION_JSON);
        } else {
            // Reflect that we talk XML and JSON, with a strong preference to JSON
            for (Preference<MediaType> preference : acceptedMediaTypes) {
                MediaType mediaType = preference.metadata
                float coefficient = 0.01f; // Given absolutely no other choice, we'll try something else...
                if (MediaType.APPLICATION_JSON.equals(mediaType)) {
                    coefficient = 1.0f;
                } else if (mediaType.includes(MediaType.APPLICATION_JSON)) {
                    coefficient = 0.95f;
                } else if (MediaType.APPLICATION_XML.equals(mediaType)) {
                    coefficient = 0.9f;
                } else if (mediaType.includes(MediaType.APPLICATION_XML)) {
                    coefficient = 0.85f;
                }
                float originalQuality = preference.quality
                float quality = originalQuality * coefficient;
                log.fine("Setting " + mediaType + " preference from " + originalQuality + " to " + quality);
                preference.quality = Math.min(1.0f, quality);
            }

            // If we have something containing JSON, but not JSON itself, make JSON slightly more preferred
            boolean hasJson = acceptedMediaTypes.any { it.metadata == MediaType.APPLICATION_JSON }
            if (!hasJson) {
                def mediaTypes = acceptedMediaTypes.findAll { it.metadata.includes(MediaType.APPLICATION_JSON) }
                def qualities = mediaTypes*.quality
                if (qualities && !qualities.isEmpty()) {
                    float maxQuality = qualities.max()
                    log.fine("Saw something containing JSON, but not JSON itself: inserting stronger preference for JSON");
                    float jsonQuality = Math.min(1.0f, maxQuality * 1.1f);
                    acceptedMediaTypes.add(new Preference<MediaType>(MediaType.APPLICATION_JSON, jsonQuality));
                }
            }
        }

        // Delegate to the previous handling
        return super.beforeHandle(request, response);
    }
}
