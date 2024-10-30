package film.api.Cosine;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;

public class CosineSimilarity {
    public static double cosineSimilarity(double[] vectorA, double[] vectorB) {
        RealVector a = new ArrayRealVector(vectorA);
        RealVector b = new ArrayRealVector(vectorB);
        try {
            return (a.dotProduct(b)) / (a.getNorm() * b.getNorm());
        } catch (ArithmeticException e) {
            return  0;
        }

    }

    public static double cosineSimilarity(RealVector vectorA, RealVector vectorB) {
        try {
            return (vectorA.dotProduct(vectorB)) / (vectorA.getNorm() * vectorB.getNorm());
        }catch (ArithmeticException e) {
            return  0;
        }
    }

    public static double cosineSimilarity(RealMatrix matrixA, RealMatrix matrixB) {
        double result = 0;
        for (int i = 0; i < matrixA.getRowDimension(); i++) {
            RealVector rowA = matrixA.getRowVector(i);
            RealVector rowB = matrixB.getRowVector(i);
            result += cosineSimilarity(rowA, rowB);
        }
        return result / matrixA.getRowDimension();
    }
}
