package com.kda.digests;

import org.apache.commons.codec.digest.DigestUtils;
import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.nexus.proxy.attributes.AbstractStorageFileItemInspector;
import org.sonatype.nexus.proxy.attributes.StorageFileItemInspector;
import org.sonatype.nexus.proxy.item.StorageFileItem;
import org.sonatype.nexus.proxy.item.StorageItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

@Component(role = StorageFileItemInspector.class, hint = "sha256-digest")
public class SHA256CalculatingInspector extends AbstractStorageFileItemInspector {

    public static String DIGEST_SHA256_KEY = "digest.sha256";

    public Set<String> getIndexableKeywords() {
        Set<String> indexableKeywords = new HashSet<String>();
        indexableKeywords.add(DIGEST_SHA256_KEY);
        return indexableKeywords;
    }

    public boolean isHandled(StorageItem item) {
        if (item instanceof StorageFileItem) {
            if (item.getItemContext().containsKey(DIGEST_SHA256_KEY)) {
                String itemValue = String.valueOf(item.getItemContext().get(DIGEST_SHA256_KEY));
                item.getRepositoryItemAttributes().put(DIGEST_SHA256_KEY, itemValue);
                return false;
            }
        }
        return true;
    }

    public void processStorageFileItem(StorageFileItem item, File file) throws Exception {
        InputStream fileInputStream = new FileInputStream(file);
        try {
            String sha256String = DigestUtils.sha256Hex(fileInputStream);
            item.getRepositoryItemAttributes().put(DIGEST_SHA256_KEY, sha256String);
        } finally {
            fileInputStream.close();
        }
    }
}