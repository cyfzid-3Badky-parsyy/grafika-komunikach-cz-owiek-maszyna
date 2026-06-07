import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class BezierGumboApp extends Application {
    static class BezierPatch {
        float[][][] points = new float[4][4][3];
    }

    @Override
    public void start(Stage stage) throws Exception {

        List<BezierPatch> patches = loadPatches("gumbo.txt");

        TriangleMesh mesh = new TriangleMesh();

        for (BezierPatch patch : patches) {
            addPatchToMesh(mesh, patch.points);
        }

        MeshView meshView = new MeshView(mesh);

        PhongMaterial material = new PhongMaterial(Color.LIGHTBLUE);
        meshView.setMaterial(material);

        meshView.setScaleX(10);
        meshView.setScaleY(10);
        meshView.setScaleZ(10);

        Group root = new Group(meshView);

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-800);

        Scene scene = new Scene(root, 1000, 800, true);
        scene.setFill(Color.GRAY);
        scene.setCamera(camera);

        // obracanie myszką
        enableMouseControl(meshView, scene);

        stage.setTitle("Bezier Gumbo Model");
        stage.setScene(scene);
        stage.show();
    }

    // ===================== Wczytywanie =====================

    private List<BezierPatch> loadPatches(String filename) throws IOException {
        List<BezierPatch> patches = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));

        int patchCount = Integer.parseInt(br.readLine().trim());

        for (int p = 0; p < patchCount; p++) {

            br.readLine(); // pomijamy "3 3"

            BezierPatch patch = new BezierPatch();

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    String[] line = br.readLine().trim().split("\\s+");

                    patch.points[i][j][0] = Float.parseFloat(line[0]);
                    patch.points[i][j][1] = Float.parseFloat(line[1]);
                    patch.points[i][j][2] = Float.parseFloat(line[2]);
                }
            }

            patches.add(patch);
        }

        br.close();
        return patches;
    }

    // ===================== Mesh =====================

    private void addPatchToMesh(TriangleMesh mesh, float[][][] controlPoints) {

        int resolution = 10;
        int baseIndex = mesh.getPoints().size() / 3;

        for (int i = 0; i <= resolution; i++) {
            float u = (float) i / resolution;

            for (int j = 0; j <= resolution; j++) {
                float v = (float) j / resolution;

                float[] p = bezierSurface(u, v, controlPoints);
                mesh.getPoints().addAll(p[0], p[1], p[2]);
            }
        }

        mesh.getTexCoords().addAll(0, 0);

        for (int i = 0; i < resolution; i++) {
            for (int j = 0; j < resolution; j++) {

                int p0 = baseIndex + i * (resolution + 1) + j;
                int p1 = p0 + 1;
                int p2 = p0 + (resolution + 1);
                int p3 = p2 + 1;

                mesh.getFaces().addAll(p0,0, p2,0, p1,0);
                mesh.getFaces().addAll(p1,0, p2,0, p3,0);
            }
        }
    }

    // ===================== Bézier =====================

    private float[] bezierSurface(float u, float v, float[][][] P) {
        float x = 0, y = 0, z = 0;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                float b = bernstein(i, u) * bernstein(j, v);

                x += b * P[i][j][0];
                y += b * P[i][j][1];
                z += b * P[i][j][2];
            }
        }

        return new float[]{x, y, z};
    }

    private float bernstein(int i, float t) {
        switch (i) {
            case 0: return (float) Math.pow(1 - t, 3);
            case 1: return 3 * t * (float) Math.pow(1 - t, 2);
            case 2: return 3 * t * t * (1 - t);
            case 3: return t * t * t;
        }
        return 0;
    }

    // ===================== Myszka =====================

    private void enableMouseControl(Node node, Scene scene) {
        final double[] anchor = new double[2];
        final double[] angle = new double[2];

        scene.setOnMousePressed(e -> {
            anchor[0] = e.getSceneX();
            anchor[1] = e.getSceneY();
        });

        scene.setOnMouseDragged(e -> {
            angle[0] += (e.getSceneY() - anchor[1]);
            angle[1] += (e.getSceneX() - anchor[0]);

            node.setRotationAxis(Rotate.X_AXIS);
            node.setRotate(angle[0]);

            node.setRotationAxis(Rotate.Y_AXIS);
            node.setRotate(angle[1]);

            anchor[0] = e.getSceneX();
            anchor[1] = e.getSceneY();
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.add(new BezierGumboApp(punkty));
        frame.setVisible(true);
        launch();
    }
}