
package cl.duoc.app1.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomErrorControllerTest {

    @Test
    void testManejarError404() {
        CustomErrorController controller = new CustomErrorController();

        HttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("jakarta.servlet.error.status_code", 404);

        RedirectAttributesModelMap redirectAttributes =
                new RedirectAttributesModelMap();

        ExtendedModelMap model = new ExtendedModelMap();

        String vista = controller.handleError(
                request,
                redirectAttributes,
                model
        );

        assertEquals("error", vista);
        assertEquals(
                "La página o evento que buscas no existe.",
                model.get("alertaPopup")
        );
    }
}
