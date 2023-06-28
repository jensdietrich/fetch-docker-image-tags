package io.github.jensdietrich.fetchdockertags;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.*;
import java.text.DateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Main program entry point, needs one argument (the name of a docker image)
 */
public class Main {


    public static final Set<String> VARIANTS = Set.of("jammy","focal","centos","ubi9-minimal","buster","slim","stretch","oracle","sid","alpine","bullseye","bookworm","jessie");

    public static final Predicate<Tag> NO_LATEST = tag -> !tag.getName().equals("latest");
    public static final Predicate<Tag> LINUX = tag -> tag.supportsOS("linux");
    public static final Predicate<Tag> ARM64 = tag -> tag.supportsArchitecture("arm64");
    public static final Predicate<Tag> JDK = tag -> tag.getName().toLowerCase().contains("jdk");
    public static final Predicate<Tag> NO_VARIANT = tag -> {
        for (String variant:VARIANTS) {
            if (tag.getName().contains(variant)) {
                return false;
            }
        }
        return true;
    };
    public static final Predicate<Tag> NO_EA = tag -> !tag.getName().contains("-ea-");
    public static final Predicate<Tag> NO_EXPERIMENTAL = tag -> !tag.getName().contains("experimental");

    public static final Predicate<Tag> ALL = NO_LATEST
        .and(LINUX)
        .and(ARM64)
        .and(JDK)
        .and(NO_VARIANT)
        .and(NO_EA)
        .and(NO_EXPERIMENTAL)
        ;


    public static void main(String[] args) throws IOException {

        String imageName = args[0];
        Preconditions.checkArgument(args.length>0,"one argument required, the name of a image, e.g. eclipse-temurin");
        String url = "https://hub.docker.com/v2/namespaces/library/repositories/"+imageName+"/tags";
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("page_size","100");
        url = urlBuilder.build().toString();

        List<Tag> tags = new ArrayList<>();
        fetchPage(url,tags);

        System.out.println("tags fetched: " + tags.size());

        List<Tag> selectedTags = tags.stream()
            .filter(ALL)
            .sorted(Comparator.comparing(Tag::getName))
            .collect(Collectors.toList());
        System.out.println("tags selected: " + selectedTags.size());

        File resultFile = new File(args[0] + ".selectedtags");
        export(selectedTags,resultFile);
        System.out.println("selected tags exported to: " + resultFile.getAbsolutePath());

        resultFile = new File(args[0] + ".alltags");
        export(tags,resultFile);
        System.out.println("all tags exported to: " + resultFile.getAbsolutePath());

    }

    private static void export(List<Tag> tags, File file) throws IOException {
        try (PrintWriter b = new PrintWriter(new FileWriter(file))){
            b.println("# tags found on docker hub, fetch timestamp: " + DateFormat.getDateTimeInstance().format(new Date()));
            for (Tag tag:tags) {
                b.println(tag.getName());
            }
        }
    }

    private static void fetchPage(String url, List<Tag> tags) throws IOException {
        System.out.println("fetching from: " + url);
        Request request = new Request.Builder()
            .url(url)
            .build();

        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        Response response = call.execute();

        response.body().charStream();

        Gson gson = new Gson();
        QueryPage responsePage = gson.fromJson(response.body().charStream(), QueryPage.class);
        List<Tag> newTags = responsePage.getResults();
        tags.addAll(newTags);
        System.out.println("\t" + newTags.size() + " tags added");

        String nextUrl = responsePage.getNext();
        if (nextUrl!=null) {
            fetchPage(nextUrl,tags);
        }
    }
}
