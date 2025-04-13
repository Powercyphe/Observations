#version 150

uniform sampler2D DiffuseSampler;
uniform vec3 InSize;
uniform float time;

in vec2 texCoord;
in vec2 fragCoord;
out vec4 fragColor;

void main() {
    vec2 uv = fragCoord / InSize.xy;

    // Wobble effect - applying displacement to texture coordinates
    vec2 wobbleOffset = vec2(
    cos(time * 3.0 + uv.y * 5.0) * 0.02,
    sin(time * 3.5 + uv.x * 5.0) * 0.02
    );
    vec2 distortedTexCoord = texCoord + wobbleOffset;

    vec4 original = texture(DiffuseSampler, distortedTexCoord);

    // Color cycling effect
    vec4 col = vec4(0.5 + 0.5 * cos(time + uv.xyx + vec3(0, 2, 4)), 0.5);
    vec4 colorEffect = mix(original, col - 0.2, cos(time));

    // Chromatic aberration
    float aberrationAmount = 0.1 + abs(uv.y / 8.0);
    vec2 distFromCenter = uv - 0.5;
    vec2 aberrated = aberrationAmount * pow(distFromCenter, vec2(3.0, 3.0));

    vec4 aberratedColor = vec4(
    texture(DiffuseSampler, distortedTexCoord - aberrated).r,
    texture(DiffuseSampler, distortedTexCoord).g,
    texture(DiffuseSampler, distortedTexCoord + aberrated).b,
    1.0
    );

    // Motion Blur
    vec4 motionBlurred = vec4(0.0);
    int samples = 5;
    float blurStrength = 0.05;
    for (int i = 0; i < samples; i++) {
        float offset = float(i) / float(samples - 1) - 0.5;
        vec2 blurCoord = texCoord + vec2(offset * blurStrength, 0.0);
        motionBlurred += texture(DiffuseSampler, blurCoord);
    }
    motionBlurred /= float(samples);


    // Make you throw uo
    fragColor = mix(motionBlurred, mix(colorEffect, aberratedColor, 0.5), 0.5);
}