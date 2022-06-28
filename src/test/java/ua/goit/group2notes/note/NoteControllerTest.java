package ua.goit.group2notes.note;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.goit.group2notes.user.UserService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("classpath:application.properties")
@ActiveProfiles({"test"})
@ContextConfiguration
class NoteControllerTest {
    @Autowired
    private WebApplicationContext context;

    @MockBean
    private NoteRepository noteRepository;
    @MockBean
    private NoteController noteController;
    private MockMvc mockMvc;
    @MockBean
    UserService userService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
    //    @Test
//    @WithMockUser(roles = "USER")
//    void testGetAllNotesReturnAllNotes() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/note/list"))
//                .andExpect(MockMvcResultMatchers.view().name("notes"))
//                .andExpect(status().isOk());
//
//    }

    @Test
    @WithMockUser(roles = "USER")
//302,на локалхост
    void testGetNotesReturnNotes() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/note/list"))
                //               .andExpect(status().isFound())
                .andDo(print())

                .andExpect(MockMvcResultMatchers.view().name("noteslist"))
                .andExpect(status().isOk());
    }

    @Test

    void testGetListOfNotesShouldReturnStatus302AndRedirectToLoginPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/note/list"))
                .andExpect(status().is(302))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));


    }
    @WithMockUser("ADMIN")
    @Test
    public void correctLoginTest() throws Exception {
        mockMvc.perform(formLogin())
                .andDo(print())
                .andExpect(authenticated());
    }
    @Test
    @WithMockUser("ADMIN")
    public void testNoteCreateForm() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/note/create"))
                .andDo(print())
//                .andExpect(model().attribute("note", new NoteDto()))
                .andExpect(status().isOk())
                .andExpect(view().name("notecreateform"));

    }


    @Test
    @WithMockUser(roles = "USER")//405
    public void testNoteAdd() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.post("/note/create")
                        .param("title", "newNote")
                        .param("text", "food for dinner")
                        .param("accessType", "PRIVATE")
                )
                .andDo(print())
                .andExpect(status().isOk())
                //               .andExpect(view().name("note/create"));
                //                .andDo(print())
                //                .andExpect(status().isCreated());
                .andExpect(redirectedUrl("/note/list")); //noteeditform

    }

    @Test
    @WithMockUser(roles = "USER") //405
    public void testNoteAddisFailShortText() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.post("/note/create")
                        .param("title", "new note")
                        .param("text", "f")
                        .param("accessType", "PRIVATE")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("note/note"));
        //    .andExpect(redirectedUrl("http://localhost/login")); ///note/list
    }
}


//    @Test
//    @WithMockUser(roles = "USER")
//    public void testNoteEdit() throws Exception {
//        mockMvc
//                .perform(MockMvcRequestBuilders.get("/note/edit"))
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.view().name("noteeditform"))
//                .andExpect(status().isOk());
//
//    }
//    @Test
//    @WithMockUser(roles = "USER")
//    void test() throws Exception {
//        NoteDao note = prepareNote();
//        when(noteRepository.findByTitle("title1")).thenReturn(List.of(note));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/note/list"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("notes"))
//                .andExpect(MockMvcResultMatchers.model().attribute("note", hasItem(allOf(
//                        hasProperty("title", equalTo(note.getTitle())),
//                        hasProperty("text", equalTo(note.getText())),
//                        hasProperty("NoteAccessType", equalTo(note.getAccessType())),
//                        hasProperty("id", equalTo(note.getId()))
//                ))));
//    }
//
//
//
//        private NoteDao prepareNote() {
//        NoteDao note = new NoteDao();
//        note.setId(UUID.randomUUID());
//        note.setTitle("Note1");
//        note.setText("goods");
//        note.setAccessType(NoteAccessType.valueOf("PRIVATE"));
//        return note;
//    }
//    private UserDao prepareUser() {
//        UserDao user = new UserDao();
//        user.setId(UUID.randomUUID());
//        user.setUsername(("Inna"));
//        user.setPassword("password");
//        user.setUserRole(UserRole.valueOf(" ROLE_USER"));
//        return user;
//    }
//
//class NoteControllerTest {
//    @Autowired
//    private WebApplicationContext context;
//
//
//    @MockBean
//    private NoteRepository noteRepository;
//
//    private MockMvc mockMvc;
//
//
//
//    @BeforeEach
//    public void setup() {
//        mockMvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply(springSecurity())
//                .build();
//    }
//
////    @Test
////    @WithMockUser(roles = "USER")
////    void testGetAllNotesReturnAllNotes() throws Exception {
////        mockMvc.perform(MockMvcRequestBuilders.get("/note/list"))
////                .andExpect(MockMvcResultMatchers.view().name("notes"))
////                .andExpect(status().isOk());
////
////    }
////
//
//    @Test
//    @WithMockUser(roles = "USER") //
//    void testGetNotesReturnNotes() throws Exception {
//        mockMvc


//                .perform(MockMvcRequestBuilders.get("/note/list"))
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.view().name("noteslist"))
//                .andExpect(status().isOk());
//
//    }
//    @Test
//    @WithMockUser(roles = "USER")//
//    void testGetListOfNotesShouldReturnStatus302AndRedirectToLoginPage() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/note/list"))
//                .andExpect(status().is(302))
//                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
//
//
//    }
//    @Test
//    @WithMockUser(roles = "USER")
//    public void testGetNoteCreate() throws Exception {
//        mockMvc
//                .perform(MockMvcRequestBuilders.get("/note/create"))
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.view().name("noteform"))
//                .andExpect(status().isOk());
//
//    }
//    @Test
//    @WithMockUser(roles = "USER")//405
//    public void testNoteAdd() throws Exception {
//        mockMvc
//                .perform(MockMvcRequestBuilders.post("/note/create")
//                        .param("title", "newNote")
//                        .param("text", "food for dinner")
//                        .param("accessType", "PRIVATE")
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("note/create"));
////                .andDo(print())
////                .andExpect(status().isCreated());
////    //            .andExpect(redirectedUrl("/note/list"));
//
//    }
//    @Test
//    @WithMockUser(roles = "USER") //405
//    public void testNoteAddisFailShortText() throws Exception {
//        mockMvc
//                .perform(MockMvcRequestBuilders.post("/note/create")
//                        .param("title", "new note")
//                        .param("text", "f")
//                        .param("accessType", "PRIVATE")
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(view().name("note/note"));
//        //    .andExpect(redirectedUrl("http://localhost/login")); ///note/list
//    }
//    @Test
//    @WithMockUser(roles = "USER")
//    public void testNoteEdit() throws Exception {
//        mockMvc
//                .perform(MockMvcRequestBuilders.get("/note/edit"))
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.view().name("note/list"))
//                .andExpect(status().isOk());
//
//    }
////    @Test
////    @WithMockUser(roles = "USER")
////    void testNoteAddOk() throws Exception {
////        NoteDao note = prepareNote();
////        when(noteRepository.findByTitle("title1")).thenReturn(List.of(note));
////
////        mockMvc.perform(MockMvcRequestBuilders.get("/note/list"))
////                .andExpect(MockMvcResultMatchers.status().isOk())
////                .andExpect(MockMvcResultMatchers.view().name("notes"))
////                .andExpect(MockMvcResultMatchers.model().attribute("note", hasItem(allOf(
////                        hasProperty("title", equalTo(note.getTitle())),
////                        hasProperty("text", equalTo(note.getText())),
////                        hasProperty("NoteAccessType", equalTo(note.getAccessType())),
////                        hasProperty("id", equalTo(note.getId()))
////                ))));
////    }
////
////
////
////    private NoteDao prepareNote() {
////        NoteDao note = new NoteDao();
////        note.setId(UUID.randomUUID());
////        note.setTitle("Note1");
////        note.setText("goods");
////        note.setAccessType(NoteAccessType.valueOf("PRIVATE"));
////        return note;
////    }
////    private UserDao prepareUser() {
////        UserDao user = new UserDao();
////        user.setId(UUID.randomUUID());
////        user.setUsername(("Inna"));
////        user.setPassword("password");


////        user.setUserRole(UserRole.valueOf(" ROLE_USER"));
////        return user;
////    }
//
//
//}
//
//