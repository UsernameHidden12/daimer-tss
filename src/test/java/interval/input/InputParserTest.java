package interval.input;

import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InputParserTest {

    private final TypeConverter<Long> typeConverter = new TypeConverter<>(Long.class);
    private InputParser<Long> underTest;
    @Mock
    private BufferedReader bufferedReader;

    @BeforeEach
    void setUp() {
        underTest = new InputParser<>(() -> bufferedReader, typeConverter);
    }

    @Test
    void givenInvalidFormat_whenReadFromStdin_thenReturnEmptyList() throws IOException {
        when(bufferedReader.readLine()).thenReturn("[10,20]");

        Optional<List<List<Long>>> actual = underTest.readFromStdin();

        assertThat(actual).isEmpty();
    }

    @Test
    void givenInvalidFormat_whenReadFromStdin_thenWriteLogError() throws IOException {
        when(bufferedReader.readLine()).thenReturn("[10,20]");
        LogCaptor logCaptor = LogCaptor.forClass(InputParser.class);

        underTest.readFromStdin();

        assertThat(logCaptor.getErrorLogs()).containsExactly("Couldn't read stdin. Returning empty list.");
    }

    @Test
    void givenEmptyInput_whenReadFromStdin_thenDoNotLogError() throws IOException {
        when(bufferedReader.readLine()).thenReturn("");
        LogCaptor logCaptor = LogCaptor.forClass(InputParser.class);

        underTest.readFromStdin();

        assertThat(logCaptor.getErrorLogs()).isEmpty();
    }

    @Test
    void givenValidFormat_whenReadFromStdin_thenReturnList() throws IOException {
        when(bufferedReader.readLine()).thenReturn("[[10,20]]");

        Optional<List<List<Long>>> actual = underTest.readFromStdin();

        assertThat(actual).isNotEmpty().hasValue(List.of(List.of(10L, 20L)));
    }

    @Test
    void givenValidFormatWithExtraneousWhitespace_whenReadFromStdin_thenReturnList() throws IOException {
        when(bufferedReader.readLine()).thenReturn("  [  [  10  ,20  ]  ,[1900,2000]]  ");

        Optional<List<List<Long>>> actual = underTest.readFromStdin();

        assertThat(actual).isNotEmpty().hasValue(List.of(List.of(10L, 20L), List.of(1900L, 2000L)));
    }

}