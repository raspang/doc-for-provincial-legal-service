package com.lds.domain;

import static com.lds.domain.DocumentStatusTestSamples.*;
import static com.lds.domain.OfficeTestSamples.*;
import static com.lds.domain.ReceivedDocumentTestSamples.*;
import static com.lds.domain.RequestedActionTestSamples.*;
import static com.lds.domain.ResponsiblePersonTestSamples.*;
import static com.lds.domain.TypeOfDocumentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lds.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReceivedDocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReceivedDocument.class);
        ReceivedDocument receivedDocument1 = getReceivedDocumentSample1();
        ReceivedDocument receivedDocument2 = new ReceivedDocument();
        assertThat(receivedDocument1).isNotEqualTo(receivedDocument2);

        receivedDocument2.setId(receivedDocument1.getId());
        assertThat(receivedDocument1).isEqualTo(receivedDocument2);

        receivedDocument2 = getReceivedDocumentSample2();
        assertThat(receivedDocument1).isNotEqualTo(receivedDocument2);
    }

    @Test
    void requestedActionTest() {
        ReceivedDocument receivedDocument = getReceivedDocumentRandomSampleGenerator();
        RequestedAction requestedActionBack = getRequestedActionRandomSampleGenerator();

        receivedDocument.setRequestedAction(requestedActionBack);
        assertThat(receivedDocument.getRequestedAction()).isEqualTo(requestedActionBack);

        receivedDocument.requestedAction(null);
        assertThat(receivedDocument.getRequestedAction()).isNull();
    }

    @Test
    void typeOfDocumentTest() {
        ReceivedDocument receivedDocument = getReceivedDocumentRandomSampleGenerator();
        TypeOfDocument typeOfDocumentBack = getTypeOfDocumentRandomSampleGenerator();

        receivedDocument.setTypeOfDocument(typeOfDocumentBack);
        assertThat(receivedDocument.getTypeOfDocument()).isEqualTo(typeOfDocumentBack);

        receivedDocument.typeOfDocument(null);
        assertThat(receivedDocument.getTypeOfDocument()).isNull();
    }

    @Test
    void officeTest() {
        ReceivedDocument receivedDocument = getReceivedDocumentRandomSampleGenerator();
        Office officeBack = getOfficeRandomSampleGenerator();

        receivedDocument.setOffice(officeBack);
        assertThat(receivedDocument.getOffice()).isEqualTo(officeBack);

        receivedDocument.office(null);
        assertThat(receivedDocument.getOffice()).isNull();
    }

    @Test
    void responsiblePersonTest() {
        ReceivedDocument receivedDocument = getReceivedDocumentRandomSampleGenerator();
        ResponsiblePerson responsiblePersonBack = getResponsiblePersonRandomSampleGenerator();

        receivedDocument.setResponsiblePerson(responsiblePersonBack);
        assertThat(receivedDocument.getResponsiblePerson()).isEqualTo(responsiblePersonBack);

        receivedDocument.responsiblePerson(null);
        assertThat(receivedDocument.getResponsiblePerson()).isNull();
    }

    @Test
    void documentStatusTest() {
        ReceivedDocument receivedDocument = getReceivedDocumentRandomSampleGenerator();
        DocumentStatus documentStatusBack = getDocumentStatusRandomSampleGenerator();

        receivedDocument.setDocumentStatus(documentStatusBack);
        assertThat(receivedDocument.getDocumentStatus()).isEqualTo(documentStatusBack);

        receivedDocument.documentStatus(null);
        assertThat(receivedDocument.getDocumentStatus()).isNull();
    }
}
