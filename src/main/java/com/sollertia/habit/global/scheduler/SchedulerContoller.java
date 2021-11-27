package com.sollertia.habit.global.scheduler;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Controller
public class SchedulerContoller {

    @GetMapping("/scheduler")
    public String main(Model model) throws IOException {
        Stream<Path> files = Files.list(Paths.get("./logs"));
        List<Path> logNameList = files.map(Path::getFileName).sorted().collect(Collectors.toList());
        model.addAttribute("logs", logNameList);
        return "index";
    }

    @GetMapping("/scheduler/{fileName}")
    public String main(Model model, @PathVariable String fileName) throws IOException {
        File file = new File("./logs/" + fileName);
        String line = null;
        String ls = System.lineSeparator();
        BufferedReader reader = new BufferedReader(new FileReader(file));

        StringBuilder builder = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(ls);
            }
        } finally {
            reader.close();
        }
        String log = builder.toString();
        model.addAttribute("log", log);
        return "scheduler";
    }
}