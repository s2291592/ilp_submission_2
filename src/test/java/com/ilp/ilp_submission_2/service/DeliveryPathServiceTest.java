package com.ilp.ilp_submission_2.service;

import com.ilp.ilp_submission_2.data.LngLat;
import com.ilp.ilp_submission_2.data.Order;
import com.ilp.ilp_submission_2.data.Pizza;
import com.ilp.ilp_submission_2.data.Restaurant;
import com.ilp.ilp_submission_2.utils.PathfindingUtils;
import com.ilp.ilp_submission_2.validators.DeliveryValidator;
import com.ilp.ilp_submission_2.validators.RestaurantValidator;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeliveryPathServiceTest {

    @Mock
    private DeliveryValidator deliveryValidator;

    @Mock
    private RestaurantValidator restaurantValidator;

    private DeliveryPathService deliveryPathService;

    private MockedStatic<PathfindingUtils> mockedStatic;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deliveryPathService = new DeliveryPathService(deliveryValidator, restaurantValidator);
        mockedStatic = mockStatic(PathfindingUtils.class);
    }

    @AfterEach
    void tearDown() {
        mockedStatic.close();
    }

    @Test
    void testCalculatePath_ValidOrder() {
        // Mock data
        Order order = new Order();
        order.setPizzasInOrder(new Pizza[]{new Pizza("Margherita", 1000)});

        Restaurant restaurant = new Restaurant("Pizza Place",
                new LngLat(-3.192473, 55.946233),
                new java.time.DayOfWeek[]{java.time.DayOfWeek.MONDAY},
                new Pizza[]{new Pizza("Margherita", 1000)});

        LngLat start = new LngLat(-3.192473, 55.946233);
        LngLat end = new LngLat(-3.186874, 55.944494);

        Set<List<LngLat>> noFlyZones = new HashSet<>();
        noFlyZones.add(Arrays.asList(
                new LngLat(-3.190000, 55.945000),
                new LngLat(-3.191000, 55.946000),
                new LngLat(-3.192000, 55.945500)
        ));

        List<LngLat> expectedPath = Arrays.asList(
                new LngLat(-3.192473, 55.946233),
                new LngLat(-3.186874, 55.944494)
        );

        // Mock behaviors
        doNothing().when(deliveryValidator).validateOrder(order);
        when(deliveryValidator.fetchRestaurants()).thenReturn(List.of(restaurant));
        when(deliveryValidator.fetchNoFlyZones()).thenReturn(noFlyZones);
        when(restaurantValidator.getRestaurantLocation(order, List.of(restaurant))).thenReturn(start);
        when(deliveryValidator.getDeliveryLocation(order)).thenReturn(end);
        mockedStatic.when(() -> PathfindingUtils.calculatePathAvoidingNoFlyZones(start, end, noFlyZones))
                .thenReturn(expectedPath);

        // Call the method
        List<LngLat> path = deliveryPathService.calculatePath(order);

        // Assertions
        assertNotNull(path, "Path should not be null");
        assertEquals(expectedPath, path, "Path should match the expected path");

        // Verify mocks
        verify(deliveryValidator).validateOrder(order);
        verify(deliveryValidator).fetchRestaurants();
        verify(deliveryValidator).fetchNoFlyZones();
        verify(restaurantValidator).getRestaurantLocation(order, List.of(restaurant));
        verify(deliveryValidator).getDeliveryLocation(order);
    }

    @Test
    void testCalculatePath_NoValidPath() {
        // Mock data
        Order order = new Order();
        order.setPizzasInOrder(new Pizza[]{new Pizza("Margherita", 1000)});

        Restaurant restaurant = new Restaurant("Pizza Place",
                new LngLat(-3.192473, 55.946233),
                new java.time.DayOfWeek[]{java.time.DayOfWeek.MONDAY},
                new Pizza[]{new Pizza("Margherita", 1000)});

        LngLat start = new LngLat(-3.192473, 55.946233);
        LngLat end = new LngLat(-3.186874, 55.944494);

        Set<List<LngLat>> noFlyZones = new HashSet<>();
        noFlyZones.add(Arrays.asList(
                new LngLat(-3.192473, 55.946233),
                new LngLat(-3.186874, 55.944494)
        ));

        List<LngLat> expectedPath = List.of(); // No path found

        // Mock behaviors
        doNothing().when(deliveryValidator).validateOrder(order);
        when(deliveryValidator.fetchRestaurants()).thenReturn(List.of(restaurant));
        when(deliveryValidator.fetchNoFlyZones()).thenReturn(noFlyZones);
        when(restaurantValidator.getRestaurantLocation(order, List.of(restaurant))).thenReturn(start);
        when(deliveryValidator.getDeliveryLocation(order)).thenReturn(end);
        mockedStatic.when(() -> PathfindingUtils.calculatePathAvoidingNoFlyZones(start, end, noFlyZones))
                .thenReturn(expectedPath);

        // Call the method
        List<LngLat> path = deliveryPathService.calculatePath(order);

        // Assertions
        assertNotNull(path, "Path should not be null");
        assertTrue(path.isEmpty(), "Path should be empty when no valid path exists");

        // Verify mocks
        verify(deliveryValidator).validateOrder(order);
        verify(deliveryValidator).fetchRestaurants();
        verify(deliveryValidator).fetchNoFlyZones();
        verify(restaurantValidator).getRestaurantLocation(order, List.of(restaurant));
        verify(deliveryValidator).getDeliveryLocation(order);
    }
}
