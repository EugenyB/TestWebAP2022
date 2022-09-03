package ua.mk.berkut.server;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

@WebServlet(name = "StartTestServlet", urlPatterns = {"/teststart", "/testrun", "/testfinish", "/sendanswer"})
public class StartTestServlet extends javax.servlet.http.HttpServlet {

//    public static final String FILENAME = "qrus.txt";
    public static final int NUM_OF_QUESTIONS = 50;
    
    private List<Question> questionsList;
    private String dirimages;

    private ResultService service;

    @Resource(name = "jdbc/test")
    DataSource ds;

    @Override
    public void init() throws ServletException {
        super.init();
        if (ds!=null) {
            service = new ResultService(ds);
        }
        String filenames = getServletContext().getInitParameter("filename");
        int numOfBlocks = Integer.parseInt(getServletContext().getInitParameter("numofblocks"));
        //int[] questions = Stream.of(getServletContext().getInitParameter("questionsbyblocks").split(" ")).mapToInt(Integer::parseInt).toArray();
        questionsList = readBlocks(filenames,numOfBlocks);
        dirimages = getServletContext().getInitParameter("dirimages");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        switch (servletPath) {
            case "/teststart":
                starttest(request, response);
                break;
            case "/testrun":
                runtest(request, response);
                break;
            case "/testfinish":
                finishtest(request, response);
                break;
            case "/sendanswer":
                sendanswer(request, response);
            break;
            default: response.sendRedirect("index.jsp");
        }
    }

    private void sendanswer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (session.isNew() || session.getAttribute("questions")==null) {
            response.sendRedirect("index.jsp");
        } else {
            int current = (Integer) session.getAttribute("current");
            List<Question> questions = (List<Question>) session.getAttribute("questions");
            int numberQ = questions.size();
            String ans = request.getParameter("ans");
            if ("Next".equals(ans)) {
                current = (current + 1) % numberQ;
            }
            if ("Prev".equals(ans)) {
                current = (current + numberQ - 1) % numberQ;
            }
            if ("Accept".equals(ans)) {
                processCurrentAnswer(request, session, current);
                current = (current + 1) % numberQ;
            }
            if (timeIsOver(session) || "Finish".equals(ans) && "1".equals(request.getParameter("finish"))) {
                processCurrentAnswer(request, session, current);
                response.sendRedirect("testfinish");
            } else {
                session.setAttribute("current", current);
                response.sendRedirect("testrun");
            }
        }
    }

    private boolean timeIsOver(HttpSession session) {
//        LocalDateTime starttime = (LocalDateTime) session.getAttribute("starttime");
//        LocalDateTime now = LocalDateTime.now();
//        return starttime.plusMinutes(7).plusSeconds(30).isBefore(now);
        return false;
    }

    private void processCurrentAnswer(HttpServletRequest request, HttpSession session, int current) throws IOException {
        int[] answers = (int[]) session.getAttribute("answers");
        String q = request.getParameter("q");
        if (q != null) {
            int a = Integer.parseInt(q);
            answers[current] = a;
            session.setAttribute("answers", answers);
        }
    }

    private void finishtest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.isNew() || session.getAttribute("questions")==null) {
            response.sendRedirect("index.jsp");
        } else {
            List<Question> questions = (List<Question>) session.getAttribute("questions");
            int[] answers = (int[]) session.getAttribute("answers");
            List<TriBean> result = new ArrayList<>();
            int totalCorrect = 0;
            for (int i = 0; i < answers.length; i++) {
                TriBean tb = new TriBean(i, answers[i], questions.get(i).getCorrect());
                if (answers[i] == questions.get(i).getCorrect()) {
                    totalCorrect++;
                }
                result.add(tb);
            }
            Result testResult = new Result()
                    .correct(totalCorrect)
                    .total(result.size())
                    .wrong(answers.length-totalCorrect)
                    .fio((String) session.getAttribute("fio"))
                    .group((String) session.getAttribute("group"))
                    .startTime((LocalDateTime) session.getAttribute("starttime"))
                    .finishTime(LocalDateTime.now())
                    .result(result);
            request.setAttribute("res", testResult);
            service.writeToDB(testResult);

            session.invalidate();
            request.getRequestDispatcher("/finish.jsp").forward(request, response);
        }
    }

    private void runtest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session == null || session.isNew() || session.getAttribute("current") == null) {
            response.sendRedirect("index.jsp");
        } else {
            Integer current = (Integer)session.getAttribute("current");
            Integer answer = ((int[])session.getAttribute("answers"))[current];
            List<Question> questions = (List<Question>) session.getAttribute("questions");
            Question question = questions.get(current);
            String pict = question.getPicture();
            String picture = "";
            if (!pict.trim().isEmpty()){
                picture = "<img src=\""+dirimages+pict+"\" /> <br/>";
            }
            request.setAttribute("picture", picture);
            AnswerVariant av = new AnswerVariant(question.getAnswers(), answer);
            request.setAttribute("av", av);
            request.setAttribute("q", question);
            request.getRequestDispatcher("/run.jsp").forward(request, response);
        }
    }

    private void starttest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fio = request.getParameter("fio");
        String group = request.getParameter("group");
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        HttpSession session = request.getSession();
        List<Question> questions = createTest();
        session.setAttribute("questions", questions);
        session.setAttribute("group", group);
        session.setAttribute("fio", fio);
        session.setAttribute("starttime", LocalDateTime.now());
        session.setAttribute("current", 0);
        session.setAttribute("total", questions.size());
        session.setAttribute("ipaddress", ipAddress);
        int[] answers = new int[questions.size()];
        Arrays.fill(answers, -1);
        session.setAttribute("answers", answers);
        response.sendRedirect("testrun");
    }

    private List<Question> createTest() {
        int[] questions = Stream.of(getServletContext().getInitParameter("questionsbyblocks").split(" ")).mapToInt(Integer::parseInt).toArray();
        // 10 10 15 15
        List<Question> result = new ArrayList<>(questionsList);
        List<Question> selected = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Collections.shuffle(result.subList(NUM_OF_QUESTIONS*i, NUM_OF_QUESTIONS*(i+1)));
            selected.addAll(result.subList(NUM_OF_QUESTIONS * i, NUM_OF_QUESTIONS * i + questions[i]));
        }
        for (Question question : selected) {
            question.shuffle();
        }
        return selected.subList(0, NUM_OF_QUESTIONS);
    }


    private List<Question> readBlock(String fileName) {
        List<Question> block = new ArrayList<>();
        try (InputStream openStream = new URL(fileName).openStream();  // url
            BufferedReader in = new BufferedReader(new InputStreamReader(openStream, "cp1251"))) { // url
            String question;
            while ((question=in.readLine())!=null) {
                String pictureName = in.readLine();
                String addition = in.readLine();
                String[] answers = new String[4];
                List<String> ansList = new ArrayList<>(4);
                int correct = -1;
                for (int i=0; i<answers.length; i++) {
                    ansList.add(in.readLine());
                }
                Collections.shuffle(ansList.subList(0,3));
                for (int i=0;i<answers.length; i++) {
                    String ans = ansList.get(i);
                    if (!ans.isEmpty() && ans.charAt(0)=='+') {
                        correct = i;
                        answers[i] = ans.substring(1);
                    } else answers[i] = ans;
                }
                Question q = new Question(question, pictureName, addition, answers, correct);
                block.add(q);
                in.readLine(); 
            }
            return block;
        } catch (IOException ex) {
            Logger.getLogger(StartTestServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return block;
    }

    private List<Question> readBlocks(String filenames, int numOfBlocks) {
        List<Question> result = new ArrayList<>();
        for (int i = 1; i <= numOfBlocks; i++) {
            String filename = filenames.replace('_', (char) ('0' + i));
            result.addAll(readBlock(filename));
        }
        return result;
    }

}
