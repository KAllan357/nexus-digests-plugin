package com.kda.rest;

import com.kda.digests.SHA256CalculatingInspector;
import org.codehaus.plexus.component.annotations.Component;
import org.json.simple.JSONValue;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
import org.sonatype.nexus.plugins.RestResource;
import org.sonatype.nexus.proxy.ItemNotFoundException;
import org.sonatype.nexus.proxy.attributes.inspectors.DigestCalculatingInspector;
import org.sonatype.nexus.proxy.item.StorageFileItem;
import org.sonatype.nexus.proxy.maven.ArtifactStoreHelper;
import org.sonatype.nexus.proxy.maven.ArtifactStoreRequest;
import org.sonatype.nexus.proxy.maven.MavenRepository;
import org.sonatype.nexus.proxy.maven.gav.Gav;
import org.sonatype.nexus.rest.artifact.AbstractArtifactPlexusResource;
import org.sonatype.nexus.rest.model.ArtifactResolveResourceResponse;
import org.sonatype.plexus.rest.resource.PathProtectionDescriptor;
import org.sonatype.plexus.rest.resource.PlexusResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.LinkedHashMap;

@Path(DigestRestResource.RESOURCE_URI)
@Produces({"text/plain"})
@Component(role = PlexusResource.class, hint = "DigestRestResource")
@RestResource
public class DigestRestResource extends AbstractArtifactPlexusResource {

    public static final String RESOURCE_URI = "/artifact/digests";

    @Override
    public Object getPayloadInstance() {
        return null;
    }

    @Override
    public String getResourceUri() {
        return RESOURCE_URI;
    }

    public PathProtectionDescriptor getResourceProtection() {
        return new PathProtectionDescriptor( getResourceUri(), "authcBasic" );
    }

    @GET
    @Override
    public Object get(Context context, Request request, Response response, Variant variant) throws ResourceException {
        Form form = request.getResourceRef().getQueryAsForm();
        String groupId = form.getFirstValue("g");
        String artifactId = form.getFirstValue("a");
        String version = form.getFirstValue("v");
        String extension = form.getFirstValue("e");
        String classifier = form.getFirstValue("c");
        String packaging = form.getFirstValue("p");
        String repositoryId = form.getFirstValue("r");

        ArtifactStoreRequest gavRequest = getResourceStoreRequest(request, false, false, repositoryId, groupId, artifactId, version, packaging, classifier, extension);
        try {
            MavenRepository mavenRepository = getMavenRepository(repositoryId);
            ArtifactStoreHelper artifactHelper = mavenRepository.getArtifactStoreHelper();

            Gav resolvedGav = artifactHelper.resolveArtifact(gavRequest);
            if (resolvedGav == null) {
                throw new ItemNotFoundException(gavRequest, mavenRepository);
            }
            String repositoryPath = mavenRepository.getGavCalculator().gavToPath(resolvedGav);
            StorageFileItem resolvedFile = artifactHelper.retrieveArtifact(gavRequest);

            String sha1 = resolvedFile.getRepositoryItemAttributes().get(DigestCalculatingInspector.DIGEST_SHA1_KEY);
            String sha256 = resolvedFile.getRepositoryItemAttributes().get(SHA256CalculatingInspector.DIGEST_SHA256_KEY);

            StringBuilder foo = new StringBuilder();
            foo.append("SHA1: ").append(sha1).append("\n");
            foo.append("SHA256: ").append(sha256).append("\n");

            LinkedHashMap<Object, String> list = new LinkedHashMap<Object, String>();
            list.put("SHA1", sha1);
            list.put("SHA256", sha256);

            return new StringRepresentation(JSONValue.toJSONString(list), MediaType.APPLICATION_JSON);
        } catch (Exception e) {
            handleException(request, response, e);
            return null;
        }
    }
}
